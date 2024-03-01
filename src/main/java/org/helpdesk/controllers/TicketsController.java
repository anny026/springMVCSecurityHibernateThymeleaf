package org.helpdesk.controllers;

import org.helpdesk.dao.*;
import org.helpdesk.models.*;
import org.helpdesk.models.security.CustomUserDetails;
import org.helpdesk.service.ticket.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@EnableWebMvc
@RequestMapping("/tickets")
public class TicketsController {

    Logger logger
            = Logger.getLogger(
            TicketsController.class.getName());
    private final TicketDAO ticketDAO;
    private final UserDAO userDAO;
    private final CategoryDAO  categoryDAO;
    private final CommentDAO commentDAO;
    private final HistoryDAO historyDAO;
    private final TicketService ticketService;


    @Autowired
    public TicketsController(UserDAO userDAO,
                             TicketDAO ticketDAO, CategoryDAO categoryDAO, CommentDAO commentDAO, HistoryDAO historyDAO, TicketService ticketService) {
        this.userDAO = userDAO;
        this.ticketDAO=ticketDAO;
        this.categoryDAO = categoryDAO;
        this.commentDAO = commentDAO;
        this.historyDAO = historyDAO;
        this.ticketService = ticketService;
        logger.log(Level.INFO, "TicketController************");
    }

    @GetMapping()
    public String allTickets(Model model,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        final CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customUserDetails.getUser();  // или передавать principal
        logger.log(Level.INFO, "This is principal   "+principal.getUser());
        logger.log(Level.INFO, "This is customUserDetails   "+customUserDetails.getUser());
        model.addAttribute("username", customUserDetails.getUser().getFirstName());
        model.addAttribute("user", customUserDetails.getUser());
        model.addAttribute("urgencyEnum", Urgency.values());
        model.addAttribute("stateEnum", State.values());
        if (Role.values()[user.getRole_id()-1]==Role.ROLE_EMPLOYEE) {
            logger.log(Level.INFO, Role.values()[user.getRole_id()-1]+"==========="+Role.ROLE_EMPLOYEE);
            model.addAttribute("tickets", ticketService.getTicketEmployee(user));}
     else if (Role.values()[user.getRole_id()-1]==Role.ROLE_MANAGER) {
            logger.log(Level.INFO, Role.values()[user.getRole_id()-1]+"==========="+Role.ROLE_MANAGER);
            model.addAttribute("tickets", ticketService.getTicketManager(user));}
        else {
            logger.log(Level.INFO,   Role.values()[user.getRole_id()-1]+"==========="+Role.ROLE_ENGINEER);
            model.addAttribute("tickets", ticketService.getTicketEngineer(user));
        }
        return "ticket/allTickets";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("ticket", ticketDAO.show(id));
        return "ticket/show";
    }

    @Secured({ "ROLE_EMPLOYEE", "ROLE_MANAGER" })
    @GetMapping("/create")
    public String getCreatePage(Model model,
                               @ModelAttribute("ticket") Ticket ticket) {
        model.addAttribute("categories", categoryDAO.getCategories());
        return "ticket/createTicket";
    }


    @PostMapping(params = "submit")
    public String createNew(@ModelAttribute("ticket") @Valid Ticket ticket,
                            BindingResult bindingResult,
                            Model model,
                            @RequestParam(value = "text", required = false) String textComment,
                            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        model.addAttribute("categories", categoryDAO.getCategories());//лишнее?
        if (bindingResult.hasErrors())
            return "redirect:/tickets/create";
        User user = customUserDetails.getUser();
        logger.log(Level.INFO, "This is ticket in createNew  "+ticket);
        logger.log(Level.INFO, "This is user in createNew   "+user);
        logger.log(Level.INFO, "Comment:     ....... "+textComment);
        int id_state=2;
        int idNewTicket=ticketDAO.save(ticket, id_state, user);
        logger.log(Level.INFO, "ID saved ticket as new................"+idNewTicket);
        commentDAO.saveComment(textComment, idNewTicket, user);
        historyDAO.saveHistory("Ticket is created", "Ticket is created", idNewTicket, user);
        return "redirect:/tickets";
    }

    @PostMapping(params = "save as draft")
    public String createDaft(@ModelAttribute("ticket") @Valid Ticket ticket,
                             BindingResult bindingResult,
                             @RequestParam(value = "text", required = false) String comment,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        if (bindingResult.hasErrors())
            return "redirect:/tickets/create";
        User user = customUserDetails.getUser();
        logger.log(Level.INFO, "ticket................"+ticket);
        int id_state=1;
        int idNewTicket=ticketDAO.save(ticket, id_state, user);
        logger.log(Level.INFO, "ID saved as draft ticket................"+idNewTicket);
        commentDAO.saveComment(comment, idNewTicket, user);
        historyDAO.saveHistory("Ticket is created", "Ticket is created", idNewTicket, user);
        return "redirect:/tickets";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("ticket", ticketDAO.getTicket(id));
        model.addAttribute("categories", categoryDAO.getCategories());
        return "ticket/editTicket";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("ticket") @Valid Ticket new_Ticket, BindingResult bindingResult,
                         @PathVariable("id") int id_updatedTicket,
                         @RequestParam(value = "save edit") String param,
                         @RequestParam(value = "text", required = false) String comment,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        if (bindingResult.hasErrors())
//            return "ticket/editTicket";
        return "redirect:/tickets/{id}/edit";
        User user = customUserDetails.getUser();
        int id_status;
        logger.log(Level.INFO, "RequestParam    "+param);
        if (param.equals("Draft")) {
            id_status = 1;
        } else id_status=2;
        if (ticketDAO.getTicket(id_updatedTicket).getState_id()==1) {
            ticketDAO.update(id_updatedTicket, new_Ticket, id_status);}
        commentDAO.saveComment(comment, id_updatedTicket, user);
        historyDAO.saveHistory("Ticket is edited", "Ticket is edited", id_updatedTicket, user);
        return "redirect:/tickets";
    }

      @PatchMapping( "/action/{id}")
    public String updateStatus(@ModelAttribute("ticket") @Valid Ticket ticket, BindingResult bindingResult,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails,
                               @PathVariable("id") int id,
                               @RequestParam ("action") String action)
    {
        User user = customUserDetails.getUser();
        int id_status=1;
        logger.log(Level.INFO, "RequestParam  action  "+action);
        switch (action) {
            case("Submit"):
                id_status = 2;
                break;
            case("Approve"):
                id_status = 3;
                break;
            case("Decline"):
                id_status = 4;
                break;
            case("Cancel"):
                id_status = 7;
                break;
            case("Assign to Me"):
                id_status = 5;
                break;
            case("Done"):
                id_status = 6;
                break;
            case("Leave Feedback"):
                id_status = 0;
                return "feedback/leaveFeedback";
            case("View Feedback"):
                id_status = 0;
                return "feedback/viewFeedback";
        }
        if (id_status!=0) {
            String stateBefore= State.values()[(ticketDAO.getTicket(id).getState_id())-1].getName();
            logger.log(Level.INFO, "stateBefore   ***********  "+ stateBefore);
            String stateAfter = State.values()[id_status-1].getName();
            logger.log(Level.INFO, "stateAfter   ***********  "+ stateAfter);
            ticketService.updateStatusTicket(id, id_status, user, stateBefore, stateAfter);
        }
        return "redirect:/tickets";
    }


    @GetMapping("/{id}/overView")
    public String overView(Model model, @PathVariable("id") int id,
                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Ticket ticketById = ticketDAO.getTicket(id);
        model.addAttribute("ticket", ticketById);
        model.addAttribute("categories", categoryDAO.getCategories());
        model.addAttribute("category", categoryDAO.getCategory(ticketById.getCategory_id()));
        model.addAttribute("histories", historyDAO.getHistory(id));
        model.addAttribute("urgencyEnum", Urgency.values()[ticketById.getUrgency_id()-1].name());
        model.addAttribute("stateEnum", State.values()[ticketById.getState_id()-1].name());
        int idApproverIfExists = ticketById.getApprover_id();
        if (idApproverIfExists>0) {
        model.addAttribute("approver", userDAO.show(idApproverIfExists).getFirstName()
                +" "+userDAO.show(idApproverIfExists).getLastName()); } //сделать проверку на null
        int idAssigneeIfExists = ticketById.getAssignee_id();
        if (idAssigneeIfExists>0) {
        model.addAttribute("assignee", userDAO.show(idAssigneeIfExists).getFirstName()
                +" "+userDAO.show(idAssigneeIfExists).getFirstName());}
        return "ticket/ticketOverview";
    }

    @GetMapping("/{id}/overView/comments")
    public String getComment(Model model, @PathVariable("id") int id) {
        Ticket ticketById = ticketDAO.getTicket(id);
        model.addAttribute("ticket", ticketById);
        model.addAttribute("categories", categoryDAO.getCategories());
        model.addAttribute("category", categoryDAO.getCategory(ticketById.getCategory_id()));
        logger.log(Level.INFO, "id для comment............."+id);
        model.addAttribute("comments", commentDAO.getComments(id));
        model.addAttribute("userOnComments", userDAO);
        model.addAttribute("urgencyEnum", Urgency.values()[ticketById.getUrgency_id()-1].name());
        model.addAttribute("stateEnum", State.values()[ticketById.getState_id()-1].name());
        int idApproverIfExists = ticketById.getApprover_id();
        if (idApproverIfExists>0) {
            model.addAttribute("approver", userDAO.show(idApproverIfExists).getFirstName()
                    +" "+userDAO.show(idApproverIfExists).getLastName()); } //сделать проверку на null
        int idAssigneeIfExists = ticketById.getAssignee_id();
        if (idAssigneeIfExists>0) {
            model.addAttribute("assignee", userDAO.show(idAssigneeIfExists).getFirstName()
                    +" "+userDAO.show(idAssigneeIfExists).getFirstName());}
        return "ticket/comments";
    }

    @PostMapping("/{id}/overView/comments")
    public String addComment(@ModelAttribute("ticket") @Valid Ticket new_Ticket, BindingResult bindingResult,
                             @RequestParam(value = "text", required = false) String comment,
                             @PathVariable("id") int id_updatedTicket,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        if (bindingResult.hasErrors())
            return "ticket/comments";
        User user = customUserDetails.getUser();
        logger.log(Level.INFO, "RequestParam    "+comment);
        commentDAO.saveComment(comment, id_updatedTicket, user);
        return "redirect:/tickets/{id}/overView/comments";
    }
}

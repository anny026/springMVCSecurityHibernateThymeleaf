package org.helpdesk.dao;

import org.helpdesk.controllers.TicketsController;
import org.helpdesk.models.Ticket;
import org.helpdesk.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TicketDAO {
    private final SessionFactory sessionFactory;

    public TicketDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    Logger logger
            = Logger.getLogger(
            TicketsController.class.getName());

    @Transactional(readOnly = true)   //к удалению
    public List<Ticket> index() {
        Session session = sessionFactory.getCurrentSession();
        List<Ticket> tickets = session.createQuery("select p from Ticket p", Ticket.class)
                .getResultList();
        return tickets;
    }

    @Transactional(readOnly = true)
    public Ticket getTicket(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Ticket.class, id);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTicketsEmployee(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query query =  session.createQuery("from Ticket where owner_id=:paramName", Ticket.class);
        query.setParameter("paramName", user.getId());
        List tickets = query.getResultList();
        return tickets;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTicketsManager(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query query =  session.createQuery("from Ticket  "+
                "where " +
                " owner_id=:paramName\n" +
                "or approver_id=:paramName and state_id>2"+
                        "or user.role_id=1 and " +
                        "state_id=2 \n" ,
                Ticket.class);
        query.setParameter("paramName", user.getId());
        List tickets = query.getResultList();
        return tickets;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTicketsEngineer(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query query =  session.createQuery("from Ticket  "+
                        "where " +
                        " state_id=3" +
                        "or assignee_id=:paramName and state_id=5"+
                        "or assignee_id=:paramName and state_id=6 ",
                Ticket.class);
        query.setParameter("paramName", user.getId());
        List tickets = query.getResultList();
        return tickets;
    }

    @Transactional(readOnly = true)
    public Ticket show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Ticket.class, id);
    }
    @Transactional
    public int save(Ticket newTicket, int id_state, User user) {
        Session session = sessionFactory.getCurrentSession();
        newTicket.setUser(user);
        newTicket.setAssignee_id(0);
        newTicket.setState_id(id_state);
        java.util.Date today = new java.util.Date();
        newTicket.setCreated_on(new Date(today.getTime()));
        newTicket.setApprover_id(0);
        logger.log(Level.INFO, "ticketId  in save........"+newTicket);
        session.save(newTicket);
        session.get(Ticket.class,newTicket.getId()); //take id created ticket
        int idCreatedTicket =newTicket.getId();
        logger.log(Level.INFO, "ticketId  in save........"+idCreatedTicket);
        return idCreatedTicket;
    }

    @Transactional
    public void update(int id_updatedTicket, Ticket updatedTicket, int id_status) {
        Session session = sessionFactory.getCurrentSession();
        Ticket ticketToBeUpdated = session.get(Ticket.class, id_updatedTicket);
        ticketToBeUpdated.setName_ticket(updatedTicket.getName_ticket());
        ticketToBeUpdated.setDescription(updatedTicket.getDescription());
        ticketToBeUpdated.setDesired_resolution_date(updatedTicket.getDesired_resolution_date());
        ticketToBeUpdated.setState_id(id_status);
        ticketToBeUpdated.setUrgency_id(updatedTicket.getUrgency_id());
        ticketToBeUpdated.setCategory_id(updatedTicket.getCategory_id());
        logger.log(Level.INFO, "ticket  update........"+ticketToBeUpdated);
    }

    @Transactional
    public void updateStatusOwnTicket(int id, int id_state) {
        Session session = sessionFactory.getCurrentSession();
        Ticket ticketToBeUpdated = session.get(Ticket.class, id);
        ticketToBeUpdated.setState_id(id_state);
        logger.log(Level.INFO, "DAO updateStatusOwnTicket........"+ticketToBeUpdated);
    }

    @Transactional
    public void updateStatusByManager(int id, int id_state, int id_manager) {
        Session session = sessionFactory.getCurrentSession();
        Ticket ticketToBeUpdated = session.get(Ticket.class, id);
        ticketToBeUpdated.setState_id(id_state);
        ticketToBeUpdated.setApprover_id(id_manager);
        logger.log(Level.INFO, "DAO updateStatusByManager........"+ticketToBeUpdated);
    }

    @Transactional
    public void updateStatusByEngineer(int id, int id_state, int id_engineer) {
        Session session = sessionFactory.getCurrentSession();
        Ticket ticketToBeUpdated = session.get(Ticket.class, id);
        ticketToBeUpdated.setState_id(id_state);
        ticketToBeUpdated.setAssignee_id(id_engineer);
        logger.log(Level.INFO, "DAO updateStatusByEngineer........"+ticketToBeUpdated);
    }

}

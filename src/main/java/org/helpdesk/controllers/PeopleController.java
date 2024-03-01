package org.helpdesk.controllers;

import org.helpdesk.models.User;
import org.helpdesk.models.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
//@RequestMapping("/users")  //если понадобится расширить, добавлять, удалять user
public class PeopleController {

    Logger logger
            = Logger.getLogger(
            PeopleController.class.getName());

    @GetMapping("/login")     //настроить свою страницу login
    //todo
    public String getLoginPage(@ModelAttribute("user") User user) {
        System.out.println("////////PeopleController   getLoginPage  ");
        return "ticket/login";
    }


    @GetMapping("/success")
    public ModelAndView getSuccess(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        final CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.log(Level.INFO, "principal.getUser()  "+principal.getUser());
        System.out.println(" getSuccess//////////");
        final ModelAndView modelAndView = new ModelAndView("success");
        modelAndView.addObject("username", customUserDetails.getUser().getEmail());
        return modelAndView;
    }

}
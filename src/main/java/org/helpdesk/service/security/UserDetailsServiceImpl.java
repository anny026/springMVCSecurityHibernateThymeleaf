package org.helpdesk.service.security;

import org.helpdesk.controllers.TicketsController;
import org.helpdesk.dao.UserDAO;
import org.helpdesk.models.User;
import org.helpdesk.models.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger logger
            = Logger.getLogger(
            TicketsController.class.getName());
    private final UserDAO userDAO;

    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user;
        user = userDAO.findUserByName(s);
        logger.log(Level.INFO, "///////////loadUserByUsername   " +user);
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + s + " not found");
        }
        CustomUserDetails customUserDetails =new CustomUserDetails(user);
        logger.log(Level.INFO, "///////////loadUserByUsername  new CustomUserDetails(user)//    " +customUserDetails);
        return customUserDetails;
    }

}
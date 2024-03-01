package org.helpdesk.service.security;

import org.helpdesk.controllers.TicketsController;
import org.helpdesk.dao.UserDAO;
import org.helpdesk.models.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetailsService;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AuthProviderAdapter implements AuthenticationProvider {

    Logger logger
            = Logger.getLogger(
            AuthProviderAdapter.class.getName());
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserDetailsService service;
    @Autowired
    private UserDAO userDAO;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();                 //со страницы аутентификации
        String password = authentication.getCredentials().toString();

        logger.log(Level.INFO, "authenticate   AuthProviderAdapter//// ");
        logger.log(Level.INFO, username);
        logger.log(Level.INFO, password);

        CustomUserDetails u = (CustomUserDetails) service.loadUserByUsername(username);  //из БД
        org.helpdesk.models.User user = userDAO.findUserByName(username);

            if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials.");
        }
        UsernamePasswordAuthenticationToken tok =new UsernamePasswordAuthenticationToken(u, password, u.getAuthorities());
        logger.log(Level.INFO, "UsernamePasswordAuthenticationToken //  "+tok);
        logger.log(Level.INFO, "UsernamePasswordAuthenticationToken tok.getPrincipal//  "+tok.getPrincipal());
        return tok;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
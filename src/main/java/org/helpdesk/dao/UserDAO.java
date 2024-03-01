package org.helpdesk.dao;

import org.helpdesk.controllers.TicketsController;
import org.helpdesk.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserDAO {
    Logger logger
            = Logger.getLogger(
            TicketsController.class.getName());

    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public User show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Transactional(readOnly = true)
    public User findUserByName(String email){
        Session session = sessionFactory.getCurrentSession();
        Query query =  session.createQuery("from User where email=:paramName", User.class);
        query.setParameter("paramName", email);
        User user = (User) query.getSingleResult();
        logger.log(Level.INFO, "findUserByName     user.getEmail()   " + user.getEmail());
        logger.log(Level.INFO, "findUserByName     user.getEmail()   " + user.getEmail());
        return user;
    }
}

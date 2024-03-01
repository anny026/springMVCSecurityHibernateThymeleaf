package org.helpdesk.dao;


import org.helpdesk.controllers.TicketsController;
import org.helpdesk.models.Comment;
import org.helpdesk.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CommentDAO {
    private final SessionFactory sessionFactory;

    public CommentDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    Logger logger
            = Logger.getLogger(
            TicketsController.class.getName());

    @Transactional
    public void saveComment(String textComment, int id_updatedTicket, User user) {
        Session session = sessionFactory.getCurrentSession();
        Comment comment = new Comment();
        comment.setUser_id(user.getId());
        comment.setText(textComment);
        java.util.Date today = new java.util.Date();
        Date time = new Date(today.getTime());
        comment.setDate(time);
        comment.setTicket_id(id_updatedTicket);
        logger.log(Level.INFO, "comment to save........"+comment);
        session.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(int id_ticket) {
        Session session = sessionFactory.getCurrentSession();
        Query query =  session.createQuery("from Comment where ticket_id=:paramName", Comment.class);
        query.setParameter("paramName", id_ticket);
        List comments = query.getResultList();
        logger.log(Level.INFO, " ******** getCategories  "+comments);
        return comments;
    }

}

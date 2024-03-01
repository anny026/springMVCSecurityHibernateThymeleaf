package org.helpdesk.dao;


import org.helpdesk.models.History;
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
public class HistoryDAO {
    private final SessionFactory sessionFactory;

    public HistoryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    Logger logger
            = Logger.getLogger(
            HistoryDAO.class.getName());

    @Transactional
    public void saveHistory(String action, String description, int id_updatedTicket, User user) {
        Session session = sessionFactory.getCurrentSession();
        History history = new History();
        history.setTicket_id(id_updatedTicket);
        java.util.Date today = new java.util.Date();
        Date time = new Date(today.getTime());
        history.setDate(time);
        history.setAction(action);
        history.setUser_id(user.getId());
        history.setDescription(description);
        logger.log(Level.INFO, "History to save........"+history);
        session.save(history);
        }
    @Transactional(readOnly = true)
    public List<History> getHistory(int id_ticket) {
        Session session = sessionFactory.getCurrentSession();
        Query query =  session.createQuery("from History where ticket_id=:paramName", History.class);
        query.setParameter("paramName", id_ticket);
        List history = query.getResultList();
        logger.log(Level.INFO, " ******** getHistory  "+history);
        return history;
    }
}

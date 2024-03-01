package org.helpdesk.dao;


import org.helpdesk.models.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CategoryDAO {

    private final SessionFactory sessionFactory;

    public CategoryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
      Logger logger
            = Logger.getLogger(
            CategoryDAO.class.getName());

    @Transactional (readOnly = true)
    public Category getCategory(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Category.class, id);  }

    @Transactional (readOnly = true)
    public List<Category> getCategories() {
        Session session = sessionFactory.getCurrentSession();
        List<Category> categories = session.createQuery("select p from Category p", Category.class)
                .getResultList();
        logger.log(Level.INFO, " ******** getCategories  "+categories);
        return categories;
    }

}




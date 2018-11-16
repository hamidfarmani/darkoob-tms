package model;

import entity.Tprojecttreetype;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class projecttreetype_dao {

    public projecttreetype_dao() {

    }

    public void createProjecttreetype(Tprojecttreetype projecttreetype) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(projecttreetype);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeProjecttreetype(Tprojecttreetype projecttreetype) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(projecttreetype);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateProjecttreetype(Tprojecttreetype projecttreetype) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(projecttreetype);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tprojecttreetype> getAllProjecttreetypes() {
        List<Tprojecttreetype> projecttreetypes = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            projecttreetypes = session.createCriteria(Tprojecttreetype.class).addOrder(Order.desc("id")).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return projecttreetypes;
    }

    public Tprojecttreetype getProjecttreetypeByID(BigDecimal id) {
        List<Tprojecttreetype> projecttreetypes = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprojecttreetype where id='" + id + "'";
            projecttreetypes = session.createSQLQuery(query).addEntity(Tprojecttreetype.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projecttreetypes.isEmpty()) {
            return null;
        }
        return projecttreetypes.get(0);
    }

    public List<Tprojecttreetype> getProjecttreetypeByProjectID(BigDecimal projectID) {
        List<Tprojecttreetype> projecttreetypes = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprojecttreetype where projectid='" + projectID + "' order by (name) asc";
            projecttreetypes = session.createSQLQuery(query).addEntity(Tprojecttreetype.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projecttreetypes.isEmpty()) {
            return null;
        }
        return projecttreetypes;
    }

}

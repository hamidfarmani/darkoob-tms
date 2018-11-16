package model;

import entity.Tbugstate;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class bugstate_dao {

    public bugstate_dao() {

    }

    public void createBugstate(Tbugstate bugstate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(bugstate);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeBugstate(Tbugstate bugstate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bugstate);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateBugstate(Tbugstate bugstate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(bugstate);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tbugstate> getAllBugstates() {
        List<Tbugstate> bugstates = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            bugstates = session.createCriteria(Tbugstate.class).addOrder(Order.desc("id")).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return bugstates;
    }

    public Tbugstate getBugstateByID(BigDecimal id) {
        List<Tbugstate> bugstates = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugstate where id='" + id + "'";
            bugstates = session.createSQLQuery(query).addEntity(Tbugstate.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugstates.isEmpty()) {
            return null;
        }
        return bugstates.get(0);
    }

    public Tbugstate getBugstateByName(String name) {
        List<Tbugstate> bugstates = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugstate where name='" + name + "'";
            bugstates = session.createSQLQuery(query).addEntity(Tbugstate.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugstates.isEmpty()) {
            return null;
        }
        return bugstates.get(0);
    }

    public List<Tbugstate> getBugstateByProjectID(BigDecimal projectID) {
        List<Tbugstate> bugstates = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugstate where projectid='" + projectID + "' order by (name) asc";
            bugstates = session.createSQLQuery(query).addEntity(Tbugstate.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugstates.isEmpty()) {
            return null;
        }
        return bugstates;
    }

}

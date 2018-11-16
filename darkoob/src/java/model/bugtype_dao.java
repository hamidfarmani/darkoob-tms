package model;

import entity.Tbugtype;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class bugtype_dao {

    public bugtype_dao() {

    }

    public void createBugtype(Tbugtype bugtype) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(bugtype);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeBugtype(Tbugtype bugtype) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bugtype);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateBugtype(Tbugtype bugtype) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(bugtype);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tbugtype> getAllBugtypes() {
        List<Tbugtype> bugstates = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            bugstates = session.createCriteria(Tbugtype.class).addOrder(Order.desc("id")).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return bugstates;
    }

    public Tbugtype getBugtypeByID(BigDecimal id) {
        List<Tbugtype> bugstates = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugtype where id='" + id + "'";
            bugstates = session.createSQLQuery(query).addEntity(Tbugtype.class).list();
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

    public Tbugtype getBugtypeByName(String name) {
        List<Tbugtype> bugstates = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugtype where name='" + name + "'";
            bugstates = session.createSQLQuery(query).addEntity(Tbugtype.class).list();
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

    public List<Tbugtype> getBugtypeByProjectID(BigDecimal projectID) {
        List<Tbugtype> bugstates = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugtype where projectid='" + projectID + "' order by (name) asc";
            bugstates = session.createSQLQuery(query).addEntity(Tbugtype.class).list();
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

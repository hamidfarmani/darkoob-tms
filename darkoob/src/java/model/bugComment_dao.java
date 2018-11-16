package model;

import entity.Tbugcomment;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class bugComment_dao {

    public bugComment_dao() {

    }

    public void createBugComment(Tbugcomment bugComment) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(bugComment);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeBugComment(Tbugcomment bugComment) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bugComment);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateBugComment(Tbugcomment bugComment) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(bugComment);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public Tbugcomment getBugCommentByID(BigDecimal id) {
        List<Tbugcomment> bugComments = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugcomment where id='" + id + "'";
            bugComments = session.createSQLQuery(query).addEntity(Tbugcomment.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugComments.isEmpty()) {
            return null;
        }
        return bugComments.get(0);
    }

    public List<Tbugcomment> getBugCommentByBugID(BigDecimal id) {
        List<Tbugcomment> bugComments = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugcomment where bugid='" + id + "' and dto is null order by (id) asc";
            bugComments = session.createSQLQuery(query).addEntity(Tbugcomment.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugComments.isEmpty()) {
            return null;
        }
        return bugComments;
    }
}

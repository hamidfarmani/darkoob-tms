package model;

import entity.Tbug;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class bug_dao {

    public bug_dao() {

    }

    public void createBug(Tbug bug) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(bug);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeBug(Tbug bug) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bug);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateBug(Tbug bug) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(bug);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tbug> getAllBugs() {
        List<Tbug> bugs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbug where dto is null order by (id) desc";
            bugs = session.createSQLQuery(query).addEntity(Tbug.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugs.isEmpty()) {
            return null;
        }
        return bugs;
    }

    public Tbug getBugByID(BigDecimal id) {
        List<Tbug> bugs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbug where id='" + id + "'";
            bugs = session.createSQLQuery(query).addEntity(Tbug.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugs.isEmpty()) {
            return null;
        }
        return bugs.get(0);
    }

    public List<Tbug> getBugsByProjecttreeID(BigDecimal id) {
        List<Tbug> bugs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbug where projecttreeid='" + id + "' and dto is null";
            bugs = session.createSQLQuery(query).addEntity(Tbug.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugs.isEmpty()) {
            return null;
        }
        return bugs;
    }

    public List<Tbug> getBugsByProjectID(BigDecimal projectID) {
        List<Tbug> bugs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbug "
                    + "where iterationid in (select id from Titeration "
                    + "where versionid in (select id from tversion where projecttreeid in "
                    + "(select id from Tprojecttree where projectid='" + projectID + "' and dto is null) and dto is null) and dto is null) and dto is null order by (id) desc";
            bugs = session.createSQLQuery(query).addEntity(Tbug.class).list();
            System.out.println("bug_dao getBugByProjectID: " + query);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugs.isEmpty()) {
            return null;
        }
        return bugs;
    }

    public List<Tbug> getBugsByUserID(BigDecimal userID) {
        List<Tbug> bugs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbug where createdbyid='" + userID + "' and dto is null";
            bugs = session.createSQLQuery(query).addEntity(Tbug.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugs.isEmpty()) {
            return null;
        }
        return bugs;
    }

    public List<BigDecimal> getBugsByIterationID(BigDecimal iterationID) {
        List<BigDecimal> bugs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select id from Tbug where iterationid ='" + iterationID + "' and dto is null order by (id) desc";
            bugs = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugs.isEmpty()) {
            return null;
        }
        return bugs;
    }

    public List<Object[]> getItersBugStatusByIterationID(BigDecimal iterationID) {
        List<Object[]> bugs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select tbugstate.id,tbugstate.name,count(tbug.id) "
                    + "from tbug inner join tbugstate on tbug.stateid=tbugstate.id "
                    + "where ITERATIONID = '" + iterationID + "' "
                    + "group by tbugstate.id,tbugstate.name";
            bugs = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugs.isEmpty()) {
            return null;
        }
        return bugs;
    }
}

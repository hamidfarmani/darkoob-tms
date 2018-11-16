package model;

import entity.Titeration;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class iteration_dao {

    public iteration_dao() {

    }

    public BigDecimal createIteration(Titeration iteration) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal iterationID = null;
        try {
            tx = session.beginTransaction();
            iterationID = (BigDecimal) session.save(iteration);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        return iterationID;
    }

    public void removeIteration(Titeration iteration) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(iteration);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateIteration(Titeration iteration) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(iteration);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Titeration> getAllIterations() {
        List<Titeration> iterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Titeration where dto is null";
            iterations = session.createSQLQuery(query).addEntity(Titeration.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return iterations;
    }

    public Titeration getIterationByID(BigDecimal id) {
        List<Titeration> iterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Titeration where id='" + id + "'";
            iterations = session.createSQLQuery(query).addEntity(Titeration.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (iterations.isEmpty()) {
            return null;
        }
        return iterations.get(0);
    }

    public List<BigDecimal> getIterationsByVersionID(BigDecimal versionID) {
        List<BigDecimal> iterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select id from Titeration where versionid='" + versionID + "' and dto is null";
            iterations = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (iterations.isEmpty()) {
            return null;
        }
        return iterations;
    }

    public List<Titeration> getIterationsByProjectID(BigDecimal projectID) {
        List<Titeration> iterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Titeration where versionid in (select id from TVERSION where PROJECTTREEID in "
                    + "(select id from TPROJECTTREE where PROJECTID='" + projectID + "')) and dto is null order by id desc";
            iterations = session.createSQLQuery(query).addEntity(Titeration.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (iterations.isEmpty()) {
            return null;
        }
        return iterations;
    }

    public List<Object[]> getBugOfIterationsByProjectID(BigDecimal projectID) {
        List<Object[]> iterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select tbug.iterationid, count(tbug.id) "
                    + "from tbug left join titeration "
                    + "on tbug.iterationid=titeration.id "
                    + "left join tversion "
                    + "on titeration.versionid=tversion.id "
                    + "left join tprojecttree "
                    + "on tversion.projecttreeid=tprojecttree.id "
                    + "where tprojecttree.projectid = '" + projectID + "' "
                    + "group by iterationid "
                    + "order by (iterationid) asc";
            iterations = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (iterations.isEmpty()) {
            return null;
        }
        return iterations;
    }
}

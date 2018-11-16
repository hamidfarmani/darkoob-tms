package model;

import entity.Tversion;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class version_dao {

    public version_dao() {

    }

    public BigDecimal createVersion(Tversion version) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal versionID = null;
        try {
            tx = session.beginTransaction();
            versionID = (BigDecimal) session.save(version);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
            versionID = BigDecimal.valueOf(-1);
        }
        return versionID;
    }

    public void removeVersion(Tversion version) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(version);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateVersion(Tversion version) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(version);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tversion> getAllVersions() {
        List<Tversion> versions = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tversion where dto is null order by (id) desc";
            versions = session.createSQLQuery(query).addEntity(Tversion.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (versions.isEmpty()) {
            return null;
        }
        return versions;
    }

    public Tversion getVersionByID(BigDecimal id) {
        List<Tversion> versions = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tversion where id='" + id + "'";
            versions = session.createSQLQuery(query).addEntity(Tversion.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (versions.isEmpty()) {
            return null;
        }
        return versions.get(0);
    }

    public List<Tversion> getVersionByProjectID(BigDecimal projectID) {
        List<Tversion> versions = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tversion "
                    + "where projecttreeid in (select id from Tprojecttree where projectid='" + projectID + "') "
                    + "and dto is null order by (id) desc";
            versions = session.createSQLQuery(query).addEntity(Tversion.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (versions.isEmpty()) {
            return null;
        }
        return versions;
    }

}

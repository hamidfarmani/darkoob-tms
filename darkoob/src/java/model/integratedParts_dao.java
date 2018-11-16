package model;

import entity.Tintegratedpart;
import entity.Tprojecttree;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class integratedParts_dao {

    public integratedParts_dao() {

    }

    public BigDecimal createIntegratedPart(Tintegratedpart integratedPart) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal id = null;
        try {
            tx = session.beginTransaction();
            id = (BigDecimal) session.save(integratedPart);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
        return id;
    }

    public void removeIntegratedPart(Tintegratedpart integratedPart) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(integratedPart);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tintegratedpart> getIntegratedPartsByProjectID(BigDecimal id) {
        List<Tintegratedpart> integratedParts = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TINTEGRATEDPART where PROJECTTREEID1 in (select id from TPROJECTTREE where PROJECTID = '" + id + "' and dto is null) and dto is null";
            integratedParts = session.createSQLQuery(query).addEntity(Tintegratedpart.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (integratedParts.isEmpty()) {
            return null;
        }
        return integratedParts;
    }

    public Tintegratedpart getIntegratedPartByFirstAndSecondID(BigDecimal projecttree1, BigDecimal projecttree2) {
        List<Tintegratedpart> integratedParts = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TINTEGRATEDPART where PROJECTTREEID1 = '" + projecttree1 + "' and PROJECTTREEID2 = '" + projecttree2 + "'";
            integratedParts = session.createSQLQuery(query).addEntity(Tintegratedpart.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (integratedParts.isEmpty()) {
            return null;
        }
        return integratedParts.get(0);
    }

    public List<Tprojecttree> getFirstProjectTreeByProjectID(BigDecimal id) {
        List<Tprojecttree> integratedParts = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from tprojecttree where id in (select projecttreeid1 from tintegratedpart where dto is null group by projecttreeid1) and tprojecttree.projectid='" + id + "' and dto is null";
            integratedParts = session.createSQLQuery(query).addEntity(Tprojecttree.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (integratedParts.isEmpty()) {
            return null;
        }
        return integratedParts;
    }

    public List<Tintegratedpart> getIntegratedPartsByFirstIntegrationID(BigDecimal id) {
        List<Tintegratedpart> integratedParts = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TINTEGRATEDPART where PROJECTTREEID1 = '" + id + "'";
            System.out.println("query: " + query);
            integratedParts = session.createSQLQuery(query).addEntity(Tintegratedpart.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (integratedParts.isEmpty()) {
            return null;
        }
        return integratedParts;
    }

    public void updateIntegratedPart(Tintegratedpart integratedPart) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(integratedPart);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

}

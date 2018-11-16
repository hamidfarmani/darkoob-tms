package model;

import entity.Trequirement;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class requirement_dao {

    public requirement_dao() {

    }

    public BigDecimal createRequirement(Trequirement requirement) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal requirementID = null;
        try {
            tx = session.beginTransaction();
            requirementID = (BigDecimal) session.save(requirement);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            requirementID = BigDecimal.valueOf(-1);
        }
        return requirementID;
    }

    public void removeRequirement(Trequirement requirement) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(requirement);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateRequirement(Trequirement requirement) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(requirement);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Trequirement> getAllRequirements() {
        List<Trequirement> requirements = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Trequirement where dto is null order by (id) desc";
            requirements = session.createSQLQuery(query).addEntity(Trequirement.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return requirements;
    }

    public Trequirement getRequirementByID(BigDecimal id) {
        List<Trequirement> requirements = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Trequirement where id='" + id + "'";
            requirements = session.createSQLQuery(query).addEntity(Trequirement.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (requirements.isEmpty()) {
            return null;
        }
        return requirements.get(0);
    }

    public List<Trequirement> getRequirementByProjectID(BigDecimal projectID) {
        List<Trequirement> requirements = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Trequirement where projectid='" + projectID + "' and dto is null order by (id) desc";
            requirements = session.createSQLQuery(query).addEntity(Trequirement.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (requirements.isEmpty()) {
            return null;
        }
        return requirements;
    }

    public List<Trequirement> getRequirementByProjecttreeID(BigDecimal projecttreeID) {
        List<Trequirement> requirements = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Trequirement where id in (select requirementid from TPROJECTTREE_REQ where projecttreeid ='" + projecttreeID + "' and dto is null) and dto is null order by (id) desc";
            requirements = session.createSQLQuery(query).addEntity(Trequirement.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (requirements.isEmpty()) {
            return null;
        }
        return requirements;
    }

    public Trequirement getRequirementsBySymbol(String symbol) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Trequirement project = (Trequirement) session.createQuery("from Trequirement where symbol = :symbol and dto is null")
                .setParameter("symbol", symbol).uniqueResult();

        tx.commit();
        return project;
    }

    public List<Trequirement> getNotAssignedRequirementByProjectID(BigDecimal projectID) {
        List<Trequirement> requirements = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TREQUIREMENT where projectid='" + projectID + "' and id not in(select requirementid from TPROJECTTREE_REQ where dto is null) and dto is null";
            requirements = session.createSQLQuery(query).addEntity(Trequirement.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return requirements;
    }

    public BigDecimal getImplementedRequirementsByProjectID(BigDecimal projectID) {
        List requirements = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select count(distinct TREQUIREMENT.id) from TREQUIREMENT "
                    + "join TPROJECTTREE_REQ "
                    + "on TREQUIREMENT.ID = TPROJECTTREE_REQ.REQUIREMENTID "
                    + "join TPROJECTTREE "
                    + "on TPROJECTTREE_REQ.PROJECTTREEID = TPROJECTTREE.ID "
                    + "join TVERSION_PROJECTTREE "
                    + "on TPROJECTTREE.ID = TVERSION_PROJECTTREE.PROJECTTREEID "
                    + "where TREQUIREMENT.PROJECTID='" + projectID + "'";
            requirements = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (requirements == null) {
            return null;
        }
        return (BigDecimal) requirements.get(0);
    }

}

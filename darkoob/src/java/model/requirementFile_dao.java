package model;

import entity.Trequirementfile;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class requirementFile_dao {

    public requirementFile_dao() {

    }

    public BigDecimal createRequirementFile(Trequirementfile requirementFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal requirementFileID = null;
        try {
            tx = session.beginTransaction();
            requirementFileID = (BigDecimal) session.save(requirementFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            requirementFileID = BigDecimal.valueOf(-1);
        }
        return requirementFileID;
    }

    public void removeRequirementFile(Trequirementfile requirementFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(requirementFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateRequirementFile(Trequirementfile requirementFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(requirementFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public Trequirementfile getRequirementFileByID(BigDecimal id) {
        List<Trequirementfile> requirementFiles = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Trequirementfile where id='" + id + "'";
            requirementFiles = session.createSQLQuery(query).addEntity(Trequirementfile.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (requirementFiles.isEmpty()) {
            return null;
        }
        return requirementFiles.get(0);
    }

    public List<Trequirementfile> getRequirementFileByRequirementID(BigDecimal reqID) {
        List<Trequirementfile> requirementFiles = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Trequirementfile where requirementid='" + reqID + "' and dto is null";
            requirementFiles = session.createSQLQuery(query).addEntity(Trequirementfile.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (requirementFiles.isEmpty()) {
            return null;
        }
        return requirementFiles;
    }
}

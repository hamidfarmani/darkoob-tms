package model;

import entity.Ttestcasefile;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class testcaseFile_dao {

    public testcaseFile_dao() {

    }

    public BigDecimal createTestcaseFile(Ttestcasefile testcaseFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal testcaseFileID = null;
        try {
            tx = session.beginTransaction();
            testcaseFileID = (BigDecimal) session.save(testcaseFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            testcaseFileID = BigDecimal.valueOf(-1);
        }
        return testcaseFileID;
    }

    public void removeTestcaseFile(Ttestcasefile testcaseFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(testcaseFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateTestcaseFile(Ttestcasefile testcaseFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(testcaseFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public Ttestcasefile getTestcaseFileByID(BigDecimal id) {
        List<Ttestcasefile> testcaseFiles = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Ttestcasefile where id='" + id + "'";
            testcaseFiles = session.createSQLQuery(query).addEntity(Ttestcasefile.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseFiles.isEmpty()) {
            return null;
        }
        return testcaseFiles.get(0);
    }

    public List<Ttestcasefile> getTestcaseFileByTestcaseID(BigDecimal testcaseID) {
        List<Ttestcasefile> testcaseFiles = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Ttestcasefile where testcaseid='" + testcaseID + "' and dto is null";
            testcaseFiles = session.createSQLQuery(query).addEntity(Ttestcasefile.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return testcaseFiles;
    }
}

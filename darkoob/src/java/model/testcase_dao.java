package model;

import entity.Ttestcase;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class testcase_dao {

    public testcase_dao() {

    }

    public BigDecimal createTestcase(Ttestcase testcase) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal testcaseID = null;
        try {
            tx = session.beginTransaction();
            testcaseID = (BigDecimal) session.save(testcase);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            testcaseID = BigDecimal.valueOf(-1);
        }
        return testcaseID;
    }

    public void removeTestcase(Ttestcase testcase) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(testcase);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateTestcase(Ttestcase testcase) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(testcase);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Ttestcase> getAllTestcases() {
        List<Ttestcase> testcases = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Ttestcase where dto is null order by (id) desc";
            testcases = session.createSQLQuery(query).addEntity(Ttestcase.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return testcases;
    }

    public Ttestcase getTestcaseByID(BigDecimal id) {
        List<Ttestcase> testcases = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Ttestcase where id='" + id + "'";
            testcases = session.createSQLQuery(query).addEntity(Ttestcase.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcases.isEmpty()) {
            return null;
        }
        return testcases.get(0);
    }

    public List<Ttestcase> getTestcaseListByProjectID(BigDecimal projectID) {
        List<Ttestcase> testcases = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TTESTCASE"
                    + " where id in (select testcaseid from TTESTCASE_PROJECTTREE where PROJECTTREEID in (select id from TPROJECTTREE where PROJECTID='" + projectID + "' and dto is null) or INTEGRATEDPARTID in (select id from TINTEGRATEDPART where PROJECTTREEID1 in (select id from TPROJECTTREE where PROJECTID='39350' and dto is null) and dto is null) and dto is null) "
                    + " and dto is null order by (id) desc";
            testcases = session.createSQLQuery(query).addEntity(Ttestcase.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcases.isEmpty()) {
            return null;
        }
        return testcases;
    }

    public List<Ttestcase> getTestcaseByIterationID(BigDecimal iterationID) {
        List<Ttestcase> testcases = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from ttestcase where id in(select testcaseid from TTESTCASE_ITER where ITERATIONID='" + iterationID + "' and testdate is null and dto is null) and dto is null order by id desc";
            testcases = session.createSQLQuery(query).addEntity(Ttestcase.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcases.isEmpty()) {
            return null;
        }
        return testcases;
    }

    public List<Ttestcase> getTestcaseByIterationAndProjecttreeID(BigDecimal iterationID, BigDecimal projecttreeID) {
        List<Ttestcase> testcases = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from ttestcase where id in(select testcaseid from TTESTCASE_ITER where ITERATIONID='" + iterationID + "' and testcaseid in (select TESTCASEID from ttestcase_projecttree where ttestcase_projecttree.PROJECTTREEID ='" + projecttreeID + "' and dto is null) and testdate is null and dto is null) order by id desc";
            testcases = session.createSQLQuery(query).addEntity(Ttestcase.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcases.isEmpty()) {
            return null;
        }
        return testcases;
    }

    public List<Ttestcase> getTestcaseByProjecttreeID(BigDecimal projecttreeID) {
        List<Ttestcase> testcases = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from ttestcase where id in(select TESTCASEID from ttestcase_projecttree where (ttestcase_projecttree.PROJECTTREEID ='" + projecttreeID + "' or INTEGRATEDPARTID in (select id from TINTEGRATEDPART where PROJECTTREEID1='" + projecttreeID + "' or PROJECTTREEID2='" + projecttreeID + "' and dto is null)) and dto is null) and dto is null order by id desc";
            testcases = session.createSQLQuery(query).addEntity(Ttestcase.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcases.isEmpty()) {
            return null;
        }
        return testcases;
    }

    public List<Ttestcase> getTestcasesByUserID(BigDecimal userID) {
        List<Ttestcase> bugs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Ttestcase where createdbyid='" + userID + "' and dto is null";
            bugs = session.createSQLQuery(query).addEntity(Ttestcase.class).list();
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

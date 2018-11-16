package model;

import entity.Ttestcase;
import entity.TtestcaseIter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class testcaseIteration_dao {

    public testcaseIteration_dao() {

    }

    public void createTestcaseIteration(TtestcaseIter testcaseIteration) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(testcaseIteration);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeTestcaseIteration(TtestcaseIter testcaseIteration) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(testcaseIteration);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateTestcaseIteration(TtestcaseIter testcaseIteration) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(testcaseIteration);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<BigDecimal> getTestcaseIterationByTestCaseID(BigDecimal testcaseID) {
        List<BigDecimal> testcaseIterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select iterationid from TTESTCASE_ITER where testcaseid='" + testcaseID + "' and dto is null";
            testcaseIterations = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseIterations.isEmpty()) {
            return null;
        }
        return testcaseIterations;
    }

    public List<TtestcaseIter> getTestcaseIterationByIterationID(BigDecimal iterationID) {
        List<TtestcaseIter> testcaseIterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TTESTCASE_ITER where iterationid='" + iterationID + "' and dto is null";
            testcaseIterations = session.createSQLQuery(query).addEntity(TtestcaseIter.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseIterations.isEmpty()) {
            return null;
        }
        return testcaseIterations;
    }

    public List<BigDecimal> getTestedTestcaseIterationByIterationID(BigDecimal iterationID) {
        List<BigDecimal> testcaseIterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select testcaseid from TTESTCASE_ITER where iterationid='" + iterationID + "' and testdate is not null and dto is null";
            testcaseIterations = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseIterations.isEmpty()) {
            return null;
        }
        return testcaseIterations;
    }

    public TtestcaseIter getTestcaseIterationByTestcaseIDAndIterationID(BigDecimal testcaseID, BigDecimal iterationID) {
        List<TtestcaseIter> testcaseIterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TTESTCASE_ITER where testcaseid='" + testcaseID + "' and iterationid='" + iterationID + "' and dto is null";
            testcaseIterations = session.createSQLQuery(query).addEntity(TtestcaseIter.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseIterations.isEmpty()) {
            return null;
        }
        return testcaseIterations.get(0);
    }

    public TtestcaseIter getTestcaseIterationByIterationIDANDTestdate(BigDecimal iterationID, Date date) {
        List<TtestcaseIter> testcaseIterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TTESTCASE_ITER where iterationid='" + iterationID + "' and ";
            if (date != null) {
                query = query + "testdate='" + date + "' ";
            } else {
                query = query + "testdate is null ";
            }
            query = query + "and dto is null";

            testcaseIterations = session.createSQLQuery(query).addEntity(TtestcaseIter.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseIterations.isEmpty()) {
            return null;
        }
        return testcaseIterations.get(0);
    }

    public List<Ttestcase> getTestcaseListByIterationID(BigDecimal iterationID) {
        List<Ttestcase> testcases = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from ttestcase where id in(select testcaseid from TTESTCASE_ITER where iterationid='" + iterationID + "' and dto is null) and dto is null";
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

    public void deleteByIterationID(BigDecimal iterationID) {
        Transaction tx = null;
        try {
            List<TtestcaseIter> testcaseIterList = getTestcaseIterationByIterationID(iterationID);
            if (testcaseIterList != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < testcaseIterList.size(); i++) {
                    TtestcaseIter testcaseIter = testcaseIterList.get(i);
                    testcaseIter.setDto(new Date());
                    session.update(testcaseIter);
                }
                tx.commit();
            }
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}

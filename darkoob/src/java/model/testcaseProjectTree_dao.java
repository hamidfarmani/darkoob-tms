package model;

import entity.Ttestcase;
import entity.TtestcaseProjecttree;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class testcaseProjectTree_dao {

    public testcaseProjectTree_dao() {

    }

    public BigDecimal createTestcaseProjectTree(TtestcaseProjecttree testcaseProjecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal testcaseProjecttreeID = null;
        try {
            tx = session.beginTransaction();
            testcaseProjecttreeID = (BigDecimal) session.save(testcaseProjecttree);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            testcaseProjecttreeID = BigDecimal.valueOf(-1);
        }
        return testcaseProjecttreeID;
    }

    public void removeTestcaseProjectTree(TtestcaseProjecttree testcaseProjecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(testcaseProjecttree);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateTestcaseProjectTree(TtestcaseProjecttree testcaseProjecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(testcaseProjecttree);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void deleteByTestcaseID(BigDecimal testcaseID) {
        Transaction tx = null;
        try {
            List<TtestcaseProjecttree> testcaseProjectTreeList = getTestcaseProjectTreeByTestcaseID(testcaseID);
            if (testcaseProjectTreeList != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < testcaseProjectTreeList.size(); i++) {
                    TtestcaseProjecttree testcaseTree = testcaseProjectTreeList.get(i);
                    testcaseTree.setDto(new Date());
                    session.update(testcaseTree);
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

    public List<TtestcaseProjecttree> getTestcaseProjectTreeByTestcaseID(BigDecimal testcaseID) {
        List<TtestcaseProjecttree> testcaseProjecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TTESTCASE_PROJECTTREE where testcaseid='" + testcaseID + "' and dto is null";
            testcaseProjecttrees = session.createSQLQuery(query).addEntity(TtestcaseProjecttree.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseProjecttrees.isEmpty()) {
            return null;
        }
        return testcaseProjecttrees;
    }
    
    public List<BigDecimal> getTestcaseProjectTreeByProjectTreeID(BigDecimal projecttreeID) {
        List<BigDecimal> testcaseProjecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select testcaseid from TTESTCASE_PROJECTTREE where projecttreeid='" + projecttreeID + "' and dto is null";
            testcaseProjecttrees = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseProjecttrees.isEmpty()) {
            return null;
        }
        return testcaseProjecttrees;
    }

    public List<BigDecimal> getTestcaseProjectTreeByIntegratedpatID(BigDecimal integID) {
        List<BigDecimal> testcaseProjecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select testcaseid from TTESTCASE_PROJECTTREE where integratedpartid='" + integID + "' and dto is null";
            testcaseProjecttrees = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (testcaseProjecttrees.isEmpty()) {
            return null;
        }
        return testcaseProjecttrees;
    }

    public List<BigDecimal> getProjectTreeByTestcaseID(BigDecimal testcaseID) {
        List<BigDecimal> projecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select projecttreeid from TTESTCASE_PROJECTTREE where testcaseid='" + testcaseID + "' and dto is null";
            projecttrees = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projecttrees.isEmpty()) {
            return null;
        }
        return projecttrees;
    }
}

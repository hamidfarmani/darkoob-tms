package model;

import entity.Tprojecttree;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class projectTree_dao {

    public projectTree_dao() {

    }

    public void createProjecttree(Tprojecttree projecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(projecttree);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeProjecttree(Tprojecttree projecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(projecttree);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateProjecttree(Tprojecttree projecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(projecttree);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tprojecttree> getProjecttreeByProjectID(BigDecimal projectID) {
        List<Tprojecttree> projecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprojecttree where projectid='" + projectID + "' and dto is null order by (id) ASC";
            projecttrees = session.createSQLQuery(query).addEntity(Tprojecttree.class).list();
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

    public Tprojecttree getProjecttreeByID(BigDecimal projecttreeID) {
        List<Tprojecttree> projecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprojecttree where id='" + projecttreeID + "' and dto is null";
            projecttrees = session.createSQLQuery(query).addEntity(Tprojecttree.class).list();
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
        return projecttrees.get(0);
    }

    public Tprojecttree getProjecttreeRootByProjectID(BigDecimal projectID) {
        List<Tprojecttree> projecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprojecttree where projectid='" + projectID + "' and parentid is null and dto is null";
            projecttrees = session.createSQLQuery(query).addEntity(Tprojecttree.class).list();
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
        return projecttrees.get(0);
    }

    public List<Tprojecttree> getChildrenByParentID(BigDecimal parentID) {
        List<Tprojecttree> projecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprojecttree where parentid='" + parentID + "' order by name asc";
            projecttrees = session.createSQLQuery(query).addEntity(Tprojecttree.class).list();
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

    public List<Object[]> getNumberOfTestcaseOfProjecttrees(BigDecimal projectID) {
        List<Object[]> testcaseIterations = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from "
                    + "("
                    + "select tprojecttree.id,tprojecttree.name, count(ttestcase.id) "
                    + "from tprojecttree,TTESTCASE,TTESTCASE_PROJECTTREE "
                    + "where tprojecttree.PROJECTID='" + projectID + "' and ttestcase.id=TTESTCASE_PROJECTTREE.TESTCASEID and tprojecttree.id = TTESTCASE_PROJECTTREE.PROJECTTREEID and TTESTCASE_PROJECTTREE.DTO is null "
                    + "group by tprojecttree.id,tprojecttree.name ORDER BY count(ttestcase.id) desc"
                    + ") "
                    + "where rownum<=10";
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
}

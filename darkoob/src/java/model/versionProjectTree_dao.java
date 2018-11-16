package model;

import entity.TversionProjecttree;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class versionProjectTree_dao {

    public versionProjectTree_dao() {

    }

    public BigDecimal createVersionProjectTree(TversionProjecttree versionProjecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal versionProjecttreeID = null;
        try {
            tx = session.beginTransaction();
            versionProjecttreeID = (BigDecimal) session.save(versionProjecttree);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            versionProjecttreeID = BigDecimal.valueOf(-1);
        }
        return versionProjecttreeID;
    }

    public void removeVersionProjectTree(TversionProjecttree versionProjecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(versionProjecttree);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateVersionProjectTree(TversionProjecttree versionProjecttree) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(versionProjecttree);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void deleteByVersionID(BigDecimal versionID) {
        Transaction tx = null;
        try {
            List<TversionProjecttree> versionProjectTreeList = getVersionProjectTreeByVersionID(versionID);
            if (versionProjectTreeList != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < versionProjectTreeList.size(); i++) {
                    TversionProjecttree versionTree = versionProjectTreeList.get(i);
                    session.delete(versionTree);
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

    public List<TversionProjecttree> getVersionProjectTreeByVersionID(BigDecimal versionID) {
        List<TversionProjecttree> versionProjecttree = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tversion_PROJECTTREE where versionid='" + versionID + "'";
            versionProjecttree = session.createSQLQuery(query).addEntity(TversionProjecttree.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (versionProjecttree.isEmpty()) {
            return null;
        }
        return versionProjecttree;
    }

    public List<BigDecimal> getVersionProjectTreeByProjectTreeID(BigDecimal projectTreeID) {
        List<BigDecimal> versionProjecttree = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select versionid from Tversion_PROJECTTREE where projecttreeid='" + projectTreeID + "' and dto is null";
            versionProjecttree = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (versionProjecttree.isEmpty()) {
            return null;
        }
        return versionProjecttree;
    }

    public List<BigDecimal> getProjectTreeByVersionID(BigDecimal versionID, String type) {
        List<BigDecimal> projecttrees = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select projecttreeid from TVersion_PROJECTTREE where versionid='" + versionID + "' and type='" + type + "' and dto is null";
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

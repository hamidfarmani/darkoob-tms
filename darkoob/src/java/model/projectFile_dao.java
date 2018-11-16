package model;

import entity.Tprojectfile;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class projectFile_dao {

    public projectFile_dao() {

    }

    public BigDecimal createProjectFile(Tprojectfile projectFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal projectFileID = null;
        try {
            tx = session.beginTransaction();
            projectFileID = (BigDecimal) session.save(projectFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            projectFileID = BigDecimal.valueOf(-1);
        }
        return projectFileID;
    }

    public void removeProjectFile(Tprojectfile projectFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(projectFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateProjectFile(Tprojectfile projectFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(projectFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public Tprojectfile getProjectFileByID(BigDecimal id) {
        List<Tprojectfile> projectFiles = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprojectfile where id='" + id + "' and dto is null";
            projectFiles = session.createSQLQuery(query).addEntity(Tprojectfile.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projectFiles.isEmpty()) {
            return null;
        }
        return projectFiles.get(0);
    }

    public List<Tprojectfile> getProjectFileByProjectID(BigDecimal projectID) {
        List<Tprojectfile> projectFiles = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprojectfile where projectid='" + projectID + "' and dto is null";
            projectFiles = session.createSQLQuery(query).addEntity(Tprojectfile.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projectFiles.isEmpty()) {
            return null;
        }
        return projectFiles;
    }
}

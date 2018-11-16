package model;

import entity.Tproject;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class project_dao {

    public project_dao() {

    }

    public BigDecimal createProject(Tproject project) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal projectID = null;
        try {
            tx = session.beginTransaction();
            projectID = (BigDecimal) session.save(project);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            projectID = BigDecimal.valueOf(-1);
        }
        return projectID;
    }

    public void removeProject(Tproject project) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(project);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateProject(Tproject project) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(project);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tproject> getAllProjects() {
        List<Tproject> projects = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tproject where dto is null order by(id) desc";
            projects = session.createSQLQuery(query).addEntity(Tproject.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return projects;
    }

    public Tproject getProjectByID(BigDecimal id) {
        List<Tproject> projects = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tproject where id='" + id + "'";
            projects = session.createSQLQuery(query).addEntity(Tproject.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projects.isEmpty()) {
            return null;
        }
        return projects.get(0);
    }

    public Tproject getProjectByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Tproject project = (Tproject) session.createQuery("from Tproject where name = :name and dto is null")
                .setParameter("name", name).uniqueResult();

        tx.commit();
        return project;
    }

    public List<BigDecimal> getProjectsByOwnerID(BigDecimal ownerID) {
        List<BigDecimal> projects = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select id from Tproject where ownerid='" + ownerID + "' and dto is null";
            projects = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projects.isEmpty()) {
            return null;
        }
        return projects;
    }
}

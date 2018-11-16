package model;

import entity.TprojecttreeReq;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class requirementProjectTree_dao {

    public requirementProjectTree_dao() {

    }

    public BigDecimal createProjectTreeRequirement(TprojecttreeReq projecttreeReq) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal projecttreeReqID = null;
        try {
            tx = session.beginTransaction();
            projecttreeReqID = (BigDecimal) session.save(projecttreeReq);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            projecttreeReqID = BigDecimal.valueOf(-1);
        }
        return projecttreeReqID;
    }

    public void removeProjectTreeRequirement(TprojecttreeReq projecttreeReq) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(projecttreeReq);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateProjectTreeRequirement(TprojecttreeReq projecttreeReq) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(projecttreeReq);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<TprojecttreeReq> getProjectTreeRequirementByRequirementID(BigDecimal requirementID) {
        List<TprojecttreeReq> projecttreeReqs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TPROJECTTREE_REQ where REQUIREMENTID='" + requirementID + "' and dto is null";
            projecttreeReqs = session.createSQLQuery(query).addEntity(TprojecttreeReq.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projecttreeReqs.isEmpty()) {
            return null;
        }
        return projecttreeReqs;
    }

    public List<BigDecimal> getProjectTreeRequirementByProjectTreeID(BigDecimal projectTreeID) {
        List<BigDecimal> projecttreeReqs = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select requirementid from TPROJECTTREE_REQ where projecttreeid='" + projectTreeID + "' and dto is null";
            projecttreeReqs = session.createSQLQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (projecttreeReqs.isEmpty()) {
            return null;
        }
        return projecttreeReqs;
    }

    public void deleteByRequirementID(BigDecimal requirementID) {
        Transaction tx = null;
        try {
            List<TprojecttreeReq> projectTreeRequirementList = getProjectTreeRequirementByRequirementID(requirementID);
            if (projectTreeRequirementList != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < projectTreeRequirementList.size(); i++) {
                    TprojecttreeReq projecttreeReq = projectTreeRequirementList.get(i);
                    projecttreeReq.setDto(new Date());
                    session.update(projecttreeReq);
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

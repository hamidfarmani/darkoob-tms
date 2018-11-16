package model;

import entity.Tbugfile;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class bugFile_dao {

    public bugFile_dao() {

    }

    public BigDecimal createBugFile(Tbugfile bugFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal bugFileID = null;
        try {
            tx = session.beginTransaction();
            bugFileID = (BigDecimal) session.save(bugFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            bugFileID = BigDecimal.valueOf(-1);
        }
        return bugFileID;
    }
    
    public void updateBugFile(Tbugfile bugFile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(bugFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }


    public List<Tbugfile> getBugFileByBugID(BigDecimal bugID) {
        List<Tbugfile> bugFiles = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tbugfile where bugid='" + bugID + "' and dto is null";
            bugFiles = session.createSQLQuery(query).addEntity(Tbugfile.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (bugFiles.isEmpty()) {
            return null;
        }
        return bugFiles;
    }
}

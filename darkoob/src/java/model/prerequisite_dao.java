package model;

import entity.Tprerequisite;
import java.math.BigDecimal;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class prerequisite_dao {

    public prerequisite_dao() {

    }

    public BigDecimal createPrerequisite(Tprerequisite Tprerequisite) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal TprerequisiteID = null;
        try {
            tx = session.beginTransaction();
            TprerequisiteID = (BigDecimal) session.save(Tprerequisite);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            TprerequisiteID = BigDecimal.valueOf(-1);
        }
        return TprerequisiteID;
    }

    public void removePrerequisite(Tprerequisite Tprerequisite) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(Tprerequisite);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updatePrerequisite(Tprerequisite Tprerequisite) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(Tprerequisite);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

}

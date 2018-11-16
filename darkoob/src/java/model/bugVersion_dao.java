package model;

import entity.TbugVersion;
import java.math.BigDecimal;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class bugVersion_dao {

    public bugVersion_dao() {

    }

    public BigDecimal createBugVersion(TbugVersion bugVersion) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal bugVersionID = null;
        try {
            tx = session.beginTransaction();
            bugVersionID = (BigDecimal) session.save(bugVersion);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            bugVersionID = BigDecimal.valueOf(-1);
        }
        return bugVersionID;
    }
}

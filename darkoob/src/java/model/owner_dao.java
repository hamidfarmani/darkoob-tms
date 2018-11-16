package model;

import entity.Towner;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class owner_dao {

    public owner_dao() {

    }

    public BigDecimal createOwner(Towner owner) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        BigDecimal ownerID = null;
        try {
            tx = session.beginTransaction();
            ownerID = (BigDecimal) session.save(owner);
            tx.commit();
            return ownerID;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            return BigDecimal.valueOf(-1);
        }
    }

    public void removeOwner(Towner owner) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(owner);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateOwner(Towner owner) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(owner);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Towner> getAllOwners() {
        List<Towner> owners = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Towner where dto is null order by (id) desc";
            owners = session.createSQLQuery(query).addEntity(Towner.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (owners.isEmpty()) {
            return null;
        }
        return owners;
    }

    public Towner getOwnerByID(BigDecimal id) {
        List<Towner> owners = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Towner where id='" + id + "'";
            owners = session.createSQLQuery(query).addEntity(Towner.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (owners.isEmpty()) {
            return null;
        }
        return owners.get(0);
    }

    public Towner getOwnerByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Towner owner = (Towner) session.createQuery("from Towner where name = :name and dto is null")
                .setParameter("name", name).uniqueResult();

        tx.commit();
        return owner;
    }

}

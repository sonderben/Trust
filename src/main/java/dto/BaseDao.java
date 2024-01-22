package dto;

import com.sonderben.trust.db.HIbernateUtil;

import entity.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

abstract public class BaseDao<E extends BaseEntity,T> {
    SessionFactory sessionFactory = HIbernateUtil.INSTANCE.getSessionFactory();

    public E save( E entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.persist(entity);

        session.getTransaction().commit();
        session.close();

        return entity;
    }

     public List<E> save(List<E> entities) {
        Session  session = sessionFactory.openSession();

         session.beginTransaction();

         session.persist(entities);

         session.getTransaction().commit();
         session.close();

         return entities;

    }



    public E  delete( E entity  ) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.remove( entity );
        session.getTransaction().commit();

        session.close();
        return entity;
    }

    public E update(E entity) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.merge(entity);
        session.getTransaction().commit();

        session.close();
        return entity;
    }


    //
    public E findById(Class<E> classEntity,T id) throws Exception {
        if (id == null){
            throw new Exception(" id entity ${E::class} can't be null ");
        }
         Session session = sessionFactory.openSession();
        try {
            return session.get(classEntity, id);
        } catch ( Exception E) {
            throw new  Exception("Entity ${E::class} with $id don't exist.");
        } finally {
            session.close();
        }

    }



    /*inline fun <reified E> findAll(entityClass: Class<E>): List<E> {
        val session = sessionFactory.openSession()
        return session.createQuery("select e from ${entityClass.simpleName} e", entityClass).resultList
    }*/




}

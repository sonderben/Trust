package com.sonderben.trust.db

import com.sonderben.trust.db.entity.BaseEntity
import com.sonderben.trust.model.deleteClass
import org.hibernate.Session
import org.hibernate.SessionFactory
import java.lang.Exception

abstract class CrudRepositoryImpl<E : BaseEntity, T>  {

     var sessionFactory:SessionFactory = HIbernateUtil.getSessionFactory()

     fun save(entity: E):E {
         val session:Session = sessionFactory.openSession()
        session.use { e ->
            e.beginTransaction()
            e.persist(entity)
            e.transaction.commit()
            return entity
        }


    }

     fun save(vararg entities: E):List<E> {
        val session:Session = sessionFactory.openSession()
        session.use { e ->
            e.beginTransaction()
            e.persist(entities)
            e.transaction.commit()
            return entities.toList()
        }

    }



      fun  delete( entity:E  ):E {
         val session = sessionFactory.openSession()

         session.beginTransaction()
         session.remove( entity )
         session.transaction.commit()

         session.close()
          return entity
    }

     fun update(entity: E):E {
         val session = sessionFactory.openSession()

         session.beginTransaction()
         session.merge(entity)
         session.transaction.commit()

         session.close()
         return entity
    }


    //
     inline fun<reified E> findById(id:T): E {
         if (id == null){
             throw Exception(" id entity ${E::class} can't be null ")
         }
        val session: Session = sessionFactory.openSession()
        try {
            return session.get(E::class.java, id)
        } catch (e: Exception) {
            throw Exception("Entity ${E::class} with $id don't exist.")
        } finally {
            session.close()
        }

    }

    inline fun<reified  E> findAll():List<E>{
        val session = sessionFactory.openSession()
        return session.createQuery("select e from ${E::class.java.simpleName} e",E::class.java).resultList
    }






}
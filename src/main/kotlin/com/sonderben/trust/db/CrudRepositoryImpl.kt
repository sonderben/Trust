package com.sonderben.trust.db

import org.hibernate.Session
import org.hibernate.SessionFactory
import java.lang.Exception

abstract class CrudRepositoryImpl<E , T>  {

     protected var sessionFactory:SessionFactory = HIbernateUtil.getSessionFactory()

     fun save(entity: E):E {
         val session:Session = sessionFactory.openSession()

         session.beginTransaction()
         session.merge(entity)
         session.transaction.commit()

         session.close()
            return entity



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








}
package com.sonderben.trust.db

import com.sonderben.trust.constant.Action
import com.sonderben.trust.model.Product
import com.sonderben.trust.model.Role
import com.sonderben.trust.model.Screen

import com.sonderben.trust.model.deleteClass
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object HIbernateUtil {
    private val sessionFactory:SessionFactory = try {
        Configuration().configure("hibernate.cfg.xml")
            .addAnnotatedClass(deleteClass::class.java)
            .addAnnotatedClass( Role::class.java )
            .addAnnotatedClass( Screen::class.java )
            .addAnnotatedClass( Action::class.java )
            .addAnnotatedClass( Product::class.java )
            .buildSessionFactory()

    }catch ( ex: Throwable){
        throw ExceptionInInitializerError(ex)
    }

    fun getSessionFactory() = sessionFactory
}
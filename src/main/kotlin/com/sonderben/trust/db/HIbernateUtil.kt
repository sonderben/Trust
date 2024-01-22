package com.sonderben.trust.db

import com.sonderben.trust.constant.Action
import com.sonderben.trust.model.Role
import com.sonderben.trust.model.Screen
import entity.CategoryEntity
import entity.CustomerEntity

import entity.EmployeeEntity
import entity.InvoiceEntity
import entity.ProductEntity
import entity.ScheduleEntity
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

object HIbernateUtil {
    private val sessionFactory:SessionFactory = try {
        Configuration().configure("hibernate.cfg.xml")
            .addAnnotatedClass( ScheduleEntity::class.java )
            .addAnnotatedClass( Role::class.java )
            .addAnnotatedClass( Screen::class.java )
            .addAnnotatedClass( Action::class.java )
            .addAnnotatedClass( CategoryEntity::class.java )
            .addAnnotatedClass( ProductEntity::class.java )
            .addAnnotatedClass( CustomerEntity::class.java )
            .addAnnotatedClass( InvoiceEntity::class.java )
            .addAnnotatedClass( EmployeeEntity::class.java )

            .buildSessionFactory()

    }catch ( ex: Throwable){
        throw ExceptionInInitializerError(ex)
    }

    fun getSessionFactory() = sessionFactory
}
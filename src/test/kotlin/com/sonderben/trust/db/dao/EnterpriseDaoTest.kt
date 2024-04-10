package com.sonderben.trust.db.dao

import com.sonderben.trust.CategoryEnum
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.model.Role
import entity.AdminEntity
import entity.EmployeeEntity
import entity.EnterpriseEntity
import entity.ScheduleEntity
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

class EnterpriseDaoTest {

    @Test
    fun save() {

        val adm = AdminEntity("name","lastName","male",
            "b@gmail.com","221", Calendar.getInstance(),"1234")

        val enter = EnterpriseEntity(
            null,"acra","pv","1", Calendar.getInstance(),
            "1","GENERAL",adm,"1"
        )
        val e = EnterpriseDao()
        Database.connect().use {
            assertTrue( e.save(enter,it) !=null )
        }

    }

    @Test
    fun findAll() {

        val e = EnterpriseDao()
        assertTrue( e.findAll().isNotEmpty() )
    }
}
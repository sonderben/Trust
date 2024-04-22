package com.sonderben.trust.db.dao

import Database
import entity.AdminEntity
import entity.EnterpriseEntity
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class EnterpriseDaoTest {

    @Test
    fun save() {

        /*val adm = AdminEntity("name","lastName","male",
            "b@gmail.com","221", Calendar.getInstance(),"1234")

        val enter = EnterpriseEntity(
            null,"acra","pv","1", Calendar.getInstance(),
            "1","GENERAL",adm,"1"
        )
        val e = EnterpriseDao()
        Database.connect().use {
            assertTrue( e.save(enter,it) !=null )
        }*/

    }

    @Test
    fun findAll() {

        /*val e = EnterpriseDao()
        assertTrue( e.findAll().isNotEmpty() )*/
    }
}
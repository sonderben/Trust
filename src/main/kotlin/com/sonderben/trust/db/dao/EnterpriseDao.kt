package com.sonderben.trust.db.dao


import Database
import com.sonderben.trust.Util
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.db.SqlDml.INSERT_ENTERPRISE
import com.sonderben.trust.db.SqlDml.SELECT_ENTERPRISE
import com.sonderben.trust.db.SqlDml.UPDATE_ENTERPRISE
import entity.*
import java.sql.Connection
import java.sql.Timestamp
import java.util.*

class EnterpriseDao : CrudDao<EnterpriseEntity> {




    /**
     * create  default role,category,customer in this order.
     */
    private fun createDefaultEntity(connection: Connection) {
        val defaultCustomer = CustomerEntity("0000", "Default", "0000", "Customer",
            "No binary", "0000", "0000", "0000", Calendar.getInstance(), 0)

        val defaultSellerRole = Role("Seller", mutableListOf(ScreenEnum.SALE))

        val defaultCategory = CategoryEntity("0000", "General", 0.0)

        val customerDao = CustomerDao()
        val roleDao = RoleDao()
        val categoryDao = CategoryDao()

        customerDao.save( defaultCustomer,connection )
        roleDao.save( defaultSellerRole,connection )
        categoryDao.save( defaultCategory,connection )

    }

    override fun save(entity: EnterpriseEntity, connection: Connection): EnterpriseEntity? {
        connection.autoCommit = false

        val employee = entity.adminEntity
        if (employee.role == null) {
            throw Exception("Role can not be null")
        }


            val empDao = AdministratorDao()

            val emp = empDao.save(entity.adminEntity, connection)


            if (emp == null) {
                connection.rollback()
                return null
            }


            connection.prepareStatement(INSERT_ENTERPRISE).use { ps2 ->
                ps2.setString(1, entity.name)
                ps2.setString(2, entity.direction)
                ps2.setString(3, entity.telephone)
                ps2.setTimestamp(4, Timestamp(entity.foundation.time.time))
                ps2.setString(5, entity.website)
                ps2.setString(6, entity.categoryString)
                ps2.setString(7, entity.invoiceTemplateHtml)
                ps2.setLong(8, emp.id)
                if (ps2.executeUpdate() > 0) {
                    connection.commit()
                    connection.autoCommit = true
                    entity.id = Database.getLastId( connection )
                    createDefaultEntity( connection )
                    return entity
                } else {
                    connection.rollback()
                    connection.autoCommit = false

                }
            }



        return null

    }

    override fun delete(idEntity: Long, connection: Connection): Long? {
        return null
    }

    override fun findById(iEntity: Long, connection: Connection): EnterpriseEntity? {
        return null
    }


    override fun findAll(): List<EnterpriseEntity> {

        val tempList = mutableListOf<EnterpriseEntity>()




        Database.connect().use { connection ->
            connection.createStatement().use { statement ->

                statement.executeQuery(SELECT_ENTERPRISE).use { resultSet ->
                    while (resultSet.next()) {
                        //val role = Role()
                       // role.id = resultSet.getLong("ri")
                        //role.name = resultSet.getString("rm")

                        val adm = AdminEntity()
                        adm.apply {
                            id = resultSet.getLong("adi")
                            firstName = resultSet.getString("adf")
                            lastName = resultSet.getString("adl")
                            genre = resultSet.getString("adg")
                            email = resultSet.getString("ade")
                            telephone = resultSet.getString("adt")
                            birthDay = Util.timeStampToCalendar(resultSet.getTimestamp("adb"))
                            password = resultSet.getString("adp")
                        }

                        //enterprise.id as enid , enterprise.name as enn,enterprise.direction as end,enterprise.telephone as ent,enterprise.foundation as enf ,enterprise.website as enw,
                        //enterprise.category as enc ,enterprise.invoiceTemplateHtml as eni
                        tempList.add(
                            EnterpriseEntity(
                                resultSet.getLong("enid"),
                                resultSet.getString("enn"),
                                resultSet.getString("end"),
                                resultSet.getString("ent"),
                                Util.timeStampToCalendar(resultSet.getTimestamp("enf")),
                                resultSet.getString("enw"),
                                resultSet.getString("enc"),
                                adm,
                                resultSet.getString("eni")
                            )
                        )

                    }
                }

            }
        }


        return tempList
    }

    override fun update(entity: EnterpriseEntity, connection: Connection): EnterpriseEntity? {
        connection.autoCommit = false


        val administratorDao = AdministratorDao()
        val emp = administratorDao.update( entity.adminEntity, connection )
        if (emp == null) {
            println("m nulllllllll")
            connection.rollback()
            return null
        }



        connection.prepareStatement(UPDATE_ENTERPRISE).use { ps2 ->
            ps2.setString(1, entity.name)
            ps2.setString(2, entity.direction)
            ps2.setString(3, entity.telephone)
            ps2.setTimestamp(4, Timestamp(entity.foundation.time.time))
            ps2.setString(5, entity.website)
            ps2.setString(6, entity.categoryString)
            ps2.setString(7, entity.invoiceTemplateHtml)
            ps2.setLong(8, entity.id)

            if (ps2.executeUpdate() > 0) {
                connection.commit()
                connection.autoCommit = true
                return entity
            } else {
                connection.rollback()
                connection.autoCommit = false
                return null
            }
        }


    }


}

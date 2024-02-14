package com.sonderben.trust.db.dao

import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import entity.EnterpriseEntity
import javafx.collections.FXCollections
import java.sql.Timestamp

object EnterpriseDao:CrudDao<EnterpriseEntity> {
    val enterprises = FXCollections.observableArrayList<EnterpriseEntity>()
    override fun save(entity: EnterpriseEntity): Boolean {
        val employee = entity.employee
        if (employee.role == null || entity.employee.id == null){
            throw Exception("Role or Role.id can not be null")
        }
        val insertEmployee = buildString {
            append("Insert into ${SqlCreateTables.employees} ")//passport
            append("(bankAccount,direction,email,firstName,genre,lastName,passport,password,telephone,userName,birthDay,id_role) ")
            append("values (?,?,?,?,?,?,?,?,?,?,?,?) ")
        }


        val insertIntoSchedule = buildString {
            append("INSERT INTO ${SqlCreateTables.schedules }")
            append(" (workDay,start_hour,end_hour,id_employee) ")
            append(" values( ?,?,?,?);")
        }



        val insertEnterprise = buildString {
            append("Insert into ${SqlCreateTables.enterprise} ")//passport
            append("(name,direction,telephone,foundation,website,category,invoiceTemplate,invoiceTemplateHtml,id_employee) ")
            append("values (?,?,?,?,?,?,?,?,?,) ")
        }
        var lastIdEmployeeAdded = 0L
        Database.connect().use { connection ->
            connection.autoCommit = false
            connection.prepareStatement(insertEmployee).use {preparedStatement ->
                preparedStatement.setString(1,employee.bankAccount)
                preparedStatement.setString(2,employee.direction)
                preparedStatement.setString(3,employee.email)
                preparedStatement.setString(4,employee.firstName)
                preparedStatement.setString(5,employee.genre)
                preparedStatement.setString(6,employee.lastName)
                preparedStatement.setString(7,employee.passport)
                preparedStatement.setString(8,employee.password)
                preparedStatement.setString(9,employee.telephone)
                preparedStatement.setString(10,employee.userName)
                preparedStatement.setTimestamp(11, Timestamp(employee.birthDay.time.time))
                preparedStatement.setLong(12,employee.role.id)

                val rowCount = preparedStatement.executeUpdate()


                if (rowCount>0){
                    lastIdEmployeeAdded = Database.getLastId()
                    entity.id = lastIdEmployeeAdded

                    for (schedule in employee.schedules){
                        connection.prepareStatement(insertIntoSchedule).use {ps ->
                            ps.setInt(1,schedule.workDay)
                            ps.setFloat(2,schedule.startHour)
                            ps.setFloat(3,schedule.endHour)
                            ps.setLong(4,lastIdEmployeeAdded)
                            val rowCount2 = ps.executeUpdate()
                            if (rowCount2<0){
                                throw Exception(" unable to add  table: (${SqlCreateTables.schedules})")
                            }else{
                                schedule.id = Database.getLastId()
                            }
                        }
                    }
                }


                if( rowCount<=0)
                    throw Exception("cannot add employee")

                connection.prepareStatement(insertEnterprise).use { ps2->
                    ps2.setString(1,"name")
                    ps2.setString(2,"direction")
                    ps2.setString(3,"telephone")
                    ps2.setString(4,"foundation")
                    ps2.setString(5,"website")
                    ps2.setString(6,"category")
                    ps2.setString(7,"invoiceTemplate")
                    ps2.setString(8,"invoiceTemplateHtml")
                    ps2.setString(9,"id_employee")
                    if (ps2.executeUpdate()>0){
                        connection.commit()
                        connection.autoCommit = true
                        return true
                    }else
                        return false
                }





            }
        }





    }

    override fun delete(idEntity: Long): Boolean {
       return true
    }

    override fun findById(iEntity: Long): EnterpriseEntity? {
        return null
    }

    override fun findAll(): Boolean {
        Database.connect().use { connection ->
            connection.createStatement().use { statement ->
               val result = statement.executeQuery("SELECT * from ${SqlCreateTables.enterprise}")

                while (result.next()){
                    enterprises.add(
                        EnterpriseEntity(
                            result.getString("name"),
                            result.getString("direction"),
                            result.getString("telephone"),
                            Util.timeStampToCalendar( result.getTimestamp("foundation") ),
                            result.getString("website"),
                            result.getString("category"),
                            EmployeeDao.findById(result.getLong("id_employee")),
                            result.getString("invoiceTemplate"),
                            result.getString("invoiceTemplateHtml"),
                        )
                    )
                }
            }
        }
        return true
    }

    override fun update(entity: EnterpriseEntity): Boolean {
        return true
    }
}
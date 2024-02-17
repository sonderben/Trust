package com.sonderben.trust.db.dao


import Database
import Database.DATABASE_NAME
import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import com.sonderben.trust.model.Role
import entity.EmployeeEntity
import entity.EnterpriseEntity
import javafx.collections.FXCollections
import java.sql.Timestamp

object EnterpriseDao:CrudDao<EnterpriseEntity> {
    val enterprises = FXCollections.observableArrayList<EnterpriseEntity>()
    //var DATABASE_NAME = ""


    init {
       try {
           findAll()
       }catch (e:Exception){}
    }
    override fun save(entity: EnterpriseEntity): Boolean {
        Database.connect(DATABASE_NAME).autoCommit = false

        val employee = entity.employee
        if (employee.role == null ){
            throw Exception("Role can not be null")
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
            append("Insert into ${SqlCreateTables.enterprise} ")
            append("(name,direction,telephone,foundation,website,category,invoiceTemplate,invoiceTemplateHtml,id_employee) ")
            append("values (?,?,?,?,?,?,?,?,?) ")
        }
        var lastIdEmployeeAdded = 0L
        Database.connect(DATABASE_NAME).use { connection ->
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
                preparedStatement.setLong(12,saveRole(employee.role) )

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
                                connection.rollback()
                                connection.autoCommit = true
                                throw Exception(" unable to add  table: (${SqlCreateTables.schedules})")
                            }else{
                                schedule.id = Database.getLastId()
                            }
                        }
                    }
                }else{
                    connection.rollback()
                    connection.autoCommit = true
                    throw Exception("cannot add employee")
                }




                connection.prepareStatement(insertEnterprise).use { ps2->
                    ps2.setString(1,entity.name)
                    ps2.setString(2,entity.direction)
                    ps2.setString(3,entity.telephone)
                    ps2.setTimestamp(4,Timestamp(entity.foundation.time.time))
                    ps2.setString(5,entity.website)
                    ps2.setString(6,"PHARMACY")
                    ps2.setString(7,entity.invoiceTemplate)
                    ps2.setString(8,entity.invoiceTemplateHtml)
                    ps2.setLong(9,lastIdEmployeeAdded)
                    if (ps2.executeUpdate()>0){
                        connection.commit()
                        connection.autoCommit = true
                        return true
                    }else{
                        connection.rollback()
                        connection.autoCommit = false
                        throw Exception("cannot add enterprise")
                    }
                }


            }
        }

        /*val insertTrustEnterpriseInfo = "insert into ${SqlCreateTables.trustEnterpriseInfo} (name, path) values (?,?)"

        Database.connect( Database.TRUST_DB ).use { connection ->
            connection.prepareStatement( insertTrustEnterpriseInfo ).use { preparedStatement ->
                preparedStatement.setString(1, entity.name)
                preparedStatement.setString(2,entity.name)
                val rowCount = preparedStatement.executeUpdate()
                if (rowCount<=0) throw Exception("can not add enterpriseEnfo")
            }

        }*/


    }

    override fun delete(idEntity: Long): Boolean {
       return true
    }

    override fun findById(iEntity: Long): EnterpriseEntity? {
        return null
    }

    private fun saveRole(role:Role):Long{
          val insertRole = buildString {
            append("INSERT INTO ")
            append(SqlCreateTables.roles)
            append(" (name) values (?)")
    }
          val insertScreen = buildString {
            append(" INSERT INTO ")
            append(SqlCreateTables.screen)
            append(" (screenEnum,actions,id_role) values (?,?,?)")
        }
        val con = Database.connect(DATABASE_NAME)
        val ps = con.prepareStatement(insertRole)
        ps.setString(1,role.name)
        val rowCount = ps.executeUpdate()
        if (rowCount<=0) {
            con.rollback()
            con.autoCommit = true
            throw Exception(" can not add role")
        }
        val roleId = Database.getLastId()
        role.id = roleId

        for (screen in role.screens){
            val ps2=con.prepareStatement(insertScreen)
            ps2.setString(1,screen.screen.name)
            ps2.setString(2,screen.actions.map { it.name }.joinToString ("," ) )
            ps2.setLong(3,roleId)

            val rowCount2 = ps2.executeUpdate()
            if (rowCount2<=0) {
                con.rollback()
                con.autoCommit = true
                throw Exception("can't add screen: $screen")
            }

        }
        return roleId
    }

    override fun findAll(): Boolean {
        val selectAll = """
            SELECT enterprise.name as ne,enterprise.direction as ed,enterprise.telephone as et,enterprise.foundation as ef ,enterprise.website as ew,
            enterprise.category as ec ,enterprise.invoiceTemplate as ei,enterprise.invoiceTemplateHtml as ein,
            Employee.birthDay as empb,Employee.bankAccount as empbank,Employee.direction as empd,Employee.email as empe,Employee.firstName as empf,Employee.lastName as empl,
            Employee.genre as empg,Employee.passport as empp,Employee.password as emppwd,Employee.telephone as emptel,Employee.userName as empu,
            Roles.name as rm,Roles.id as ri
            from enterprise
            INNER JOIN Employee on enterprise.id_employee == Employee.id
            INNER join Roles on Roles.id = Employee.id_role
        """.trimIndent()
        Database.connect(DATABASE_NAME).use { connection ->
            connection.createStatement().use { statement ->
               val result = statement.executeQuery(selectAll)


                 Database.connect(DATABASE_NAME).use { connection->
                     connection.createStatement().use { statement ->

                         statement.executeQuery(selectAll).use { resultSet ->

                             if (resultSet.next()){
                                 val role = Role()
                                 role.id = resultSet.getLong("ri")
                                 role.name = resultSet.getString("rm")

                                 val emp = EmployeeEntity(
                                     resultSet.getString("empf"),
                                     resultSet.getString("empp"),
                                     resultSet.getString("empl"),
                                     resultSet.getString("empg"),
                                     resultSet.getString("empd"),
                                     resultSet.getString("empe"),
                                     resultSet.getString("emptel"),
                                     Util.timeStampToCalendar( resultSet.getTimestamp("empb")),
                                     resultSet.getString("empbank"),
                                     resultSet.getString("empu"),
                                     resultSet.getString("emppwd"),
                                     role,
                                     listOf()
                                 )
                                 enterprises.add(
                                     EnterpriseEntity(
                                         result.getString("ne"),
                                         result.getString("ed"),
                                         result.getString("et"),
                                         Util.timeStampToCalendar( result.getTimestamp("ef") ),
                                         result.getString("ew"),
                                         result.getString("ec"),
                                         emp,
                                         result.getString("ei"),
                                         result.getString("ein"),
                                     )
                                 )
                                 //println("enterprise: "+enterprises)
                             }
                         }

                     }
                 }

            }
        }
        return true
    }

    override fun update(entity: EnterpriseEntity): Boolean {
        return true
    }
}
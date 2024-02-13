package com.sonderben.trust.db.dao

import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import entity.EmployeeEntity
import javafx.collections.FXCollections
import java.sql.Timestamp


object EmployeeDao : CrudDao<EmployeeEntity> {


   var employees = FXCollections.observableArrayList<EmployeeEntity>()
    init {
        findAll()
    }
    override fun save(entity: EmployeeEntity): Boolean {
        if (entity.role == null || entity.role.id == null){
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
        var lastIdEmployeeAdded = 0L
        Database.connect().use { connection ->
            connection.autoCommit = false
            connection.prepareStatement(insertEmployee).use {preparedStatement ->
                preparedStatement.setString(1,entity.bankAccount)
                preparedStatement.setString(2,entity.direction)
                preparedStatement.setString(3,entity.email)
                preparedStatement.setString(4,entity.firstName)
                preparedStatement.setString(5,entity.genre)
                preparedStatement.setString(6,entity.lastName)
                preparedStatement.setString(7,entity.passport)
                preparedStatement.setString(8,entity.password)
                preparedStatement.setString(9,entity.telephone)
                preparedStatement.setString(10,entity.userName)
                preparedStatement.setTimestamp(11,Timestamp(entity.birthDay.time.time))
                preparedStatement.setLong(12,entity.role.id)

                val rowCount = preparedStatement.executeUpdate()


                if (rowCount>0){
                    lastIdEmployeeAdded = Database.getLastId()
                    entity.id = lastIdEmployeeAdded

                    for (schedule in entity.schedules){
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
                employees.add( entity )
                connection.commit()

                return rowCount>0


            }
        }

    }

    override fun delete(idEntity: Long): Boolean {
        Database.connect().autoCommit = false
        val deleteEmployee = "delete from ${SqlCreateTables.employees} where id = ?"
        val deleteSchedule = "delete from ${SqlCreateTables.schedules} where id_employee = ?"
        Database.connect().prepareStatement(deleteEmployee).use { ps ->
            ps.setLong(1,idEntity)
            val rowCount = ps.executeUpdate()
            if (rowCount>0){
                Database.connect().prepareStatement( deleteSchedule ).use {preparedStatement ->
                    preparedStatement.setLong(1,idEntity)
                    if (preparedStatement.executeUpdate()>0){
                        Database.connect().commit()
                        Database.connect().autoCommit = true
                        employees.removeIf { it.id==idEntity }
                        println("li delete")
                        return true
                    }else{
                        Database.connect().rollback()
                        Database.connect().autoCommit = true
                        println("li pa delete")
                    }
                }
            }else{
                println("can not delete employee")
                Database.connect().rollback()
                Database.connect().autoCommit = true
            }
        }
        return false
    }

    override fun findById(iEntity: Long): EmployeeEntity? {
        return null
    }

    override fun findAll(): Boolean {
         val selectAll = "SELECT * FROM ${SqlCreateTables.employees}"
        val tempEmployees = mutableListOf<EmployeeEntity>()
        Database.connect().use {connection ->
            connection.createStatement().use {statement ->
                statement.executeQuery(selectAll).use {resultSet ->
                    while (resultSet.next()){
                        val id = resultSet.getLong("id")
                        val employee = EmployeeEntity()
                        //String code,String firstName, String passport, String lastName, String genre, String direction,
                        // String email, String telephone, Calendar birthDay, String bankAccount, String userName,
                        // String password, Role role, List<ScheduleEntity> schedule)
                        employee.apply {
                            this.id = id
                            firstName = resultSet.getString("firstName")
                            passport = resultSet.getString("passport")
                            lastName = resultSet.getString("lastName")
                            genre = resultSet.getString("genre")
                            direction = resultSet.getString("direction")
                            email = resultSet.getString("email")
                            telephone = resultSet.getString("telephone")
                            birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                            bankAccount = resultSet.getString("bankAccount")
                            this.userName = resultSet.getString("userName")
                            this.password = resultSet.getString("password")

                            role = Database.findRolesByIdEmployee( resultSet.getLong("id_role") )
                            schedules =  Database.findScheduleByIdEmployee( id )
                        }

                        tempEmployees.add( employee )
                    }
                    employees.addAll( tempEmployees )
                    tempEmployees.clear();
                }
                println(employees)
                return true

            }

        }
    }

    fun login(userName:String,password:String):EmployeeEntity?{
        Database.connect().use {connection ->
            connection.prepareStatement("select * from ${SqlCreateTables.employees} where userName= ? and password =? ;").use { preparedStatement ->
                preparedStatement.setString(1,userName)
                preparedStatement.setString(2,password)
                preparedStatement.executeQuery().use { resultSet ->

                    val employee = EmployeeEntity()
                    val id = resultSet.getLong("id")
                    if ( id==0L){
                        println("user dont found: "+resultSet.fetchSize)
                        return null
                    }

                    employee.apply {
                        this.id = id
                        firstName = resultSet.getString("firstName")
                        passport = resultSet.getString("passport")
                        lastName = resultSet.getString("lastName")
                        genre = resultSet.getString("genre")
                        direction = resultSet.getString("direction")
                        email = resultSet.getString("email")
                        telephone = resultSet.getString("telephone")
                        birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                        bankAccount = resultSet.getString("bankAccount")
                        this.userName = resultSet.getString("userName")
                        this.password = resultSet.getString("password")

                        role = Database.findRolesByIdEmployee( resultSet.getLong("id_role") )
                        schedules =  Database.findScheduleByIdEmployee( id )
                    }
                    return employee


                }
            }
        }
    }

    override fun update(entity: EmployeeEntity): Boolean {
        return false
    }
}
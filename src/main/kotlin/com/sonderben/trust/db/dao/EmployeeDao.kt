package com.sonderben.trust.db.dao

import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import entity.EmployeeEntity
import javafx.collections.FXCollections
import java.sql.Timestamp


object EmployeeDao : CrudDao<EmployeeEntity> {


   var employees = FXCollections.observableArrayList<EmployeeEntity>()
    init {
        //findAll()
    }
    override fun save(employee: EmployeeEntity): Boolean {
        val insertEmployee = buildString {
            append("Insert into ${SqlCreateTables.employees} ")
            append("(bankAccount,direction,email,firstName,genre,lastName,passport,password,telephone,userName,birthDay) ")
            append("values (?,?,?,?,?,?,?,?,?,?,?) ")
        }
        val insertRoleEmployee = buildString {
            append("insert into ${SqlCreateTables.employeesRoles} ")
            append("(id_role,id_employee) ")
            append("values (?,?)")
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
                preparedStatement.setTimestamp(10,Timestamp(employee.birthDay.time.time))

                val rowCount = preparedStatement.executeUpdate()


                if (rowCount>0){
                    lastIdEmployeeAdded = Database.getLastId()
                    for (role in employee.roleList){
                        connection.prepareStatement(insertRoleEmployee).use {ps ->
                            ps.setLong(1,role.id)
                            ps.setLong(2,lastIdEmployeeAdded)
                             val rowCount2 = ps.executeUpdate()
                            if (rowCount2<0){
                                throw Exception(" unable to add relationship table (${SqlCreateTables.employeesRoles})")
                            }
                        }
                    }
                    for (schedule in employee.schedules){
                        connection.prepareStatement(insertIntoSchedule).use {ps ->
                            ps.setInt(1,schedule.workDay)
                            ps.setFloat(2,schedule.startHour)
                            ps.setFloat(3,schedule.endHour)
                            ps.setLong(4,lastIdEmployeeAdded)
                            val rowCount2 = ps.executeUpdate()
                            if (rowCount2<0){
                                throw Exception(" unable to add  table: (${SqlCreateTables.schedules})")
                            }
                        }
                    }
                }
                connection.commit()

                return rowCount>0


            }
        }

    }

    override fun delete(idEntity: Long): Boolean {
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
                        var employee = EmployeeEntity(
                            resultSet.getString("firstName"),
                            resultSet.getString("passport"),
                            resultSet.getString("lastName"),
                            resultSet.getString("genre"),
                            resultSet.getString("direction"),
                            resultSet.getString("email"),
                            resultSet.getString("telephone"),
                            Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") ),
                            resultSet.getString("bankAccount"),
                            resultSet.getString("userName"),
                            resultSet.getString("password"),
                            Database.findRolesByIdEmployee(id),
                            Database.findScheduleByIdEmployee( id )
                        )
                        employee.id = id
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

    override fun update(entity: EmployeeEntity): Boolean {
        return false
    }
}
package com.sonderben.trust.db.dao

import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import entity.CustomerEntity
import entity.EmployeeEntity
import javafx.collections.FXCollections
import java.sql.Timestamp


object CustomerDao : CrudDao<CustomerEntity> {


   var customers = FXCollections.observableArrayList<CustomerEntity>()
    init {
        //findAll()
    }
    override fun save(entity: CustomerEntity): Boolean {
        val insertEmployee = buildString {
            append("Insert into ${SqlCreateTables.customers} ")
            append("( birthDay,code,direction,point,email,firstName,genre,lastName,passport,telephone ) ")
            append("values (?,?,?,?,?,?,?,?,?,?) ")
        }

        Database.connect().use { connection ->
            connection.autoCommit = false
            connection.prepareStatement(insertEmployee).use {preparedStatement ->
                preparedStatement.setTimestamp(1,Timestamp(entity.birthDay.time.time))
                val tempCode = entity.code.padStart(12,'0')
                preparedStatement.setString(2,tempCode)
                preparedStatement.setString(3,entity.direction)
                preparedStatement.setLong(4,entity.point)
                preparedStatement.setString(5,entity.email)
                preparedStatement.setString(6,entity.firstName)
                preparedStatement.setString(7,entity.genre)
                preparedStatement.setString(8,entity.lastName)
                preparedStatement.setString(9,entity.passport)
                preparedStatement.setString(10,entity.telephone)


                val rowCount = preparedStatement.executeUpdate()

                if (rowCount<=0){
                    connection.rollback()

                }


                customers.add( entity )
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
                        customers.removeIf { it.id==idEntity }
                        return true
                    }else{
                        Database.connect().rollback()
                        Database.connect().autoCommit = true

                    }
                }
            }else{

                Database.connect().rollback()
                Database.connect().autoCommit = true
            }
        }
        return false
    }

    override fun findById(iEntity: Long): CustomerEntity? {
        return null
    }

    override fun findAll(): Boolean {
        val selectAll = "SELECT * FROM ${SqlCreateTables.customers}"
        val tempEmployees = mutableListOf<CustomerEntity>()
        Database.connect().use {connection ->
            connection.createStatement().use {statement ->
                statement.executeQuery(selectAll).use {resultSet ->
                    while (resultSet.next()){
                        val id = resultSet.getLong("id")
                        val customer = CustomerEntity()
                        customer.apply {
                            this.id = id
                            firstName = resultSet.getString("firstName")
                            passport = resultSet.getString("passport")
                            lastName = resultSet.getString("lastName")
                            genre = resultSet.getString("genre")
                            direction = resultSet.getString("direction")
                            email = resultSet.getString("email")
                            telephone = resultSet.getString("telephone")
                            birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                            code = resultSet.getString("code")
                            point = resultSet.getLong("point")

                        }

                        tempEmployees.add( customer )
                    }
                    customers.addAll( tempEmployees )
                    tempEmployees.clear();
                }
                println("findAll: "+customers)
                return true

            }

        }
    }



    override fun update(entity: CustomerEntity): Boolean {
        return false
    }

    fun findByCode(code: String):CustomerEntity?{
        Database.connect().use {connection ->
            connection.prepareStatement("select * from ${SqlCreateTables.customers} where code = ?").use { preparedStatement ->
                preparedStatement.setString(1,code.padStart(12,'0'))
                preparedStatement.executeQuery().use { resultSet ->

                    val customer = CustomerEntity()
                    customer.apply {
                        this.id = resultSet.getLong("id")
                        firstName = resultSet.getString("firstName")
                        passport = resultSet.getString("passport")
                        lastName = resultSet.getString("lastName")
                        genre = resultSet.getString("genre")
                        direction = resultSet.getString("direction")
                        email = resultSet.getString("email")
                        telephone = resultSet.getString("telephone")
                        birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                        this.code = resultSet.getString("code")
                        point = resultSet.getLong("point")

                    }



                    if (customer.id != null){
                        return customer
                    }



                }

            }

        }
        return null;
    }
    fun updatePoint(id:Long,point: Long): Boolean {
        val updateEmployee = "update  ${SqlCreateTables.customers} set point = point + ? where id = ?"

        Database.connect().use { connection ->

            connection.prepareStatement(updateEmployee).use {preparedStatement ->

                preparedStatement.setLong(1,point)
                preparedStatement.setLong(2,id)

                val rowCount = preparedStatement.executeUpdate()

                return rowCount > 0


            }
        }

    }
}

package com.sonderben.trust.db.dao

import Database
import com.sonderben.trust.Context
import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.DELETE_ADMIN
import com.sonderben.trust.db.SqlDml.INSERT_ADMIN
import com.sonderben.trust.db.SqlDml.UPDATE_ADMIN
import entity.AdminEntity
import java.sql.Connection
import java.sql.Timestamp


class AdministratorDao: CrudDao<AdminEntity> {

    override fun save(entity: AdminEntity, connection: Connection): AdminEntity? {






        connection.prepareStatement(INSERT_ADMIN).use { preparedStatement ->
            preparedStatement.setString(1, entity.firstName)
            preparedStatement.setString(2, entity.genre)

            preparedStatement.setString(3, entity.lastName)
            preparedStatement.setString(4, entity.password)
            preparedStatement.setString(5, entity.telephone)
            preparedStatement.setString(6, entity.userName)


            preparedStatement.setTimestamp(7, Timestamp(entity.birthDay.time.time))
            preparedStatement.setString(8, entity.email)


            val rowCount = preparedStatement.executeUpdate()


            if (rowCount > 0) {
                entity.id = Database.getLastId( connection )
                return entity

            } else {

                return null
            }




        }



    }

    override fun delete(idEntity: Long, connection: Connection): Long? {


        connection.prepareStatement(DELETE_ADMIN).use { ps ->
            ps.setLong(1, idEntity)
            val rowCount = ps.executeUpdate()
            if (rowCount > 0) {
                return idEntity

            }
            return null
        }


    }

    override fun findById(iEntity: Long, connection: Connection): AdminEntity? {


        val adm = findAll()
        return if (adm.isEmpty()){
            null
        } else if (adm[0].id == iEntity){
            adm[0]
        }else{
            throw RuntimeException("entity don't exist")
        }


    }

    override fun findAll( ): List<AdminEntity> {

        val listEmp = mutableListOf<AdminEntity>()


        Database.connect().use { connection ->
            connection.createStatement().use { statement ->



                statement.executeQuery("SELECT * FROM ${SqlDdl.administrator}").use { resultSet ->
                    while (resultSet.next()) {

                        val employee = AdminEntity()


                        employee.apply {
                            id = resultSet.getLong("id")
                            firstName = resultSet.getString("firstName")
                            lastName = resultSet.getString("lastName")
                            genre = resultSet.getString("genre")

                            email = resultSet.getString("email")
                            telephone = resultSet.getString("telephone")
                            birthDay = Util.timeStampToCalendar(resultSet.getTimestamp("birthDay"))

                            this.userName = resultSet.getString("userName")
                            this.password = resultSet.getString("password")


                        }

                        listEmp.add(employee)
                    }


                }


            }
        }



        return listEmp

    }



    override fun update(entity: AdminEntity, connection: Connection): AdminEntity? {



        connection.prepareStatement(UPDATE_ADMIN).use { preparedStatement ->
            preparedStatement.setString(1, entity.firstName)
            preparedStatement.setString(2, entity.genre)

            preparedStatement.setString(3, entity.lastName)
            preparedStatement.setString(4, entity.password)
            preparedStatement.setString(5, entity.telephone)
            preparedStatement.setString(6, entity.userName)


            preparedStatement.setTimestamp(7, Timestamp(entity.birthDay.time.time))
            preparedStatement.setString(8, entity.email)

            val rowCount = preparedStatement.executeUpdate()
            if (rowCount > 0) {

                return entity


            }


        }
        return null

    }

    fun updateCredential(password: String): Boolean {
        val updateCredential = "UPDATE ${SqlDdl.administrator} SET password = ? WHERE id = ?"

        Database.connect().use { connection ->
            connection.prepareStatement(updateCredential).use { preparedStatement ->
                preparedStatement.setString(1, password)
                preparedStatement.setLong(2, Context.currentEmployee.get().id)

                if (preparedStatement.executeUpdate() > 0) {
                    return true
                }
            }
        }
        return false

    }
}


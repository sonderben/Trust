package com.sonderben.trust.db.dao

import Database
import com.sonderben.trust.db.SqlCreateTables
import com.sonderben.trust.model.Role
import javafx.collections.FXCollections

object RoleDao {
    val roles  = FXCollections.observableArrayList<Role>()

    init {
        findAll()
    }

    private  val insertRole = buildString {
        append("INSERT INTO ")
        append(SqlCreateTables.roles)
        append(" (name) values (?)")
    }
    private  val insertScreen = buildString {
        append(" INSERT INTO ")
        append(SqlCreateTables.screen)
        append(" (screenEnum,actions,id_role) values (?,?,?)")
    }
    private const val selectAllRoles = "SELECT * FROM ${SqlCreateTables.roles}"


    fun save(role:Role){
        val conection = Database.connect()
        try {
            var roleId:Long = 0

            conection.autoCommit = false

            conection.prepareStatement( insertRole ).use { preparedStatement ->
                preparedStatement.setString(1,role.name)
                val intRow = preparedStatement.executeUpdate()
                if ( intRow>0 ){
                    roleId = Database.getLastId()
                    if (roleId==0L){
                        throw Exception("Can not get the id of the recent role")
                    }
                }else{
                    throw Exception("Role can not be saved")
                }
            }


            for (screen in role.screens){
                conection.prepareStatement( insertScreen ).use { preparedStatement ->
                    preparedStatement.setString(1,screen.screen.name)
                    preparedStatement.setString(2,screen.actions.map { it.name }.joinToString ("," ) )
                    preparedStatement.setLong(3,roleId)

                    val rowCount = preparedStatement.executeUpdate()
                    if (rowCount<=0)
                        throw Exception("can't save screen: $screen")
                }
            }
            conection.commit()
        }catch (e:Exception){
            conection.rollback()
        }finally {
            conection.autoCommit = true
            conection.close()
        }
    }

    private fun findAll() {

        val tempRoleList = mutableListOf<Role>()
        Database.connect().use { connection ->
            connection.autoCommit = true
            connection.createStatement().use { statement ->



                statement.executeQuery(selectAllRoles).use { resultSet ->

                    while ( resultSet.next() ){
                        val role = Role()
                        role.id = resultSet.getLong("id")
                        role.name = resultSet.getString("name")
                        role.screens = Database.findScreenByIdRole(role.id)

                        tempRoleList.add( role )
                    }
                    roles.addAll( tempRoleList )
                    //println("roles: "+ roles)

                }

            }

        }
    }

    private fun deleteById(){}


}
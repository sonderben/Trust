package com.sonderben.trust.db.dao

import com.sonderben.trust.db.SqlCreateTables
import com.sonderben.trust.model.Role
import entity.CategoryEntity
import javafx.collections.FXCollections
import java.lang.Exception

object RoleDao {
    val roles  = FXCollections.observableArrayList<Role>()

    init {
        findAll()
    }

    const val insertRole = "INSERT INTO ${SqlCreateTables.roles} (name) values (?)"
    const val insertScreen = " INSERT INTO ${SqlCreateTables.screen} (screenEnum,actions,id_role) values (?,?,?)"
    const val selectAll = "SELECT * FROM ${SqlCreateTables.roles}"

    public fun save(role:Role){
        val conexion = Database.connect()
        try {
            var roleId:Long = 0

            conexion.autoCommit = false

            conexion.prepareStatement( insertRole ).use { preparedStatement ->
                preparedStatement.setString(1,role.name)
                val intRow = preparedStatement.executeUpdate()
                if ( intRow>0 ){
                    roleId = Database.getLastId()
                    if (roleId==0L){
                        throw Exception("can not get the id of the recent role")
                    }
                }else{
                    throw Exception("Role can' t be add")
                }
            }


            for (screen in role.screens){
                conexion.prepareStatement( insertScreen ).use { preparedStatement ->
                    preparedStatement.setString(1,screen.screen.name)
                    preparedStatement.setString(2,screen.actions.map { it.name }.joinToString ("," ) )
                    preparedStatement.setLong(3,roleId)

                    val rowCount = preparedStatement.executeUpdate()
                    if (rowCount<=0)
                        throw Exception("can't save screen: $screen")
                }
            }
            conexion.commit()
        }catch (e:Exception){
            conexion.rollback()
        }finally {
            conexion.autoCommit = true
            conexion.close()
        }
    }

    private fun findAll() {
        val tempRoleList = mutableListOf<Role>()
        Database.connect().use { connection ->
            connection.autoCommit = true
            connection.createStatement().use { statement ->
                statement.executeQuery(selectAll).use {resultSet ->
                    while ( resultSet.next() ){
                        val role = Role()
                        role.id = resultSet.getLong("id")
                        role.name = resultSet.getString("name")
                        role.screens = Database.findScreenByIdRole(role.id)

                        tempRoleList.add( role )
                    }
                    roles.addAll( tempRoleList )

                }

            }

        }
    }

    private fun deleteById(){}


}
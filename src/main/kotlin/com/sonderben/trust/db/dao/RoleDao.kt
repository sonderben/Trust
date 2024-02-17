package com.sonderben.trust.db.dao

import Database
import Database.DATABASE_NAME
import com.sonderben.trust.db.SqlCreateTables
import com.sonderben.trust.model.Role
import javafx.collections.FXCollections

object RoleDao:CrudDao<Role> {
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


    override fun save(entity:Role):Boolean{
        var tempRole = entity
        Database.connect(DATABASE_NAME).use { connection ->
            var roleId:Long

            connection.autoCommit = false

            connection.prepareStatement( insertRole ).use { preparedStatement ->
                preparedStatement.setString(1,entity.name)
                val intRow = preparedStatement.executeUpdate()
                if ( intRow>0 ){
                    roleId = Database.getLastId()
                    tempRole.id = roleId

                    for (screen in entity.screens){
                        connection.prepareStatement( insertScreen ).use { ps ->
                            ps.setString(1,screen.screen.name)
                            ps.setString(2,screen.actions.map { it.name }.joinToString ("," ) )
                            ps.setLong(3,roleId)

                            val rowCount = ps.executeUpdate()
                            if (rowCount<=0) {
                                connection.rollback()
                                connection.autoCommit = true
                                throw Exception("can't save screen: $screen")
                            }

                        }
                    }

                    connection.commit()
                    roles.add( tempRole )
                    return true

                }else{
                    connection.rollback()
                    connection.autoCommit = true
                    throw Exception("Role can not be saved")
                }
            }


        }




    }

    override fun delete(idEntity: Long): Boolean {

        Database.connect(DATABASE_NAME).use { connection ->
            connection.prepareStatement("delete from ${SqlCreateTables.roles} where id = ?").use { preparedStatement ->
                preparedStatement.setLong(1,idEntity)
                val rowCount = preparedStatement.executeUpdate()
                if (rowCount>0){
                    roles.removeIf { it.id.equals(idEntity) }
                    return true
                }
                return false
            }

        }
    }

    override fun findById(iEntity: Long): Role? {
        return null
    }

    override fun findAll():Boolean {

        val tempRoleList = mutableListOf<Role>()
        Database.connect(DATABASE_NAME).use { connection ->
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


                }

            }

        }
        return true
    }

    override fun update(entity: Role): Boolean {
        return true
    }

    private fun deleteById(){}


}
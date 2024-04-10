package com.sonderben.trust.db.dao

import Database
import com.sonderben.trust.Context
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.db.SqlDml.DELETE_ROLE
import com.sonderben.trust.db.SqlDml.INSERT_ROLE
import com.sonderben.trust.db.SqlDml.SELECT_ALL_ROLE
import com.sonderben.trust.db.SqlDml.UPDATE_ROLE
import com.sonderben.trust.model.Role
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.sql.Connection

class RoleDao() : CrudDao<Role> {



    override fun save(entity: Role, connection: Connection): Role? {


        connection.prepareStatement(INSERT_ROLE).use { preparedStatement ->
            preparedStatement.setString(1, entity.name)
            preparedStatement.setString(2, entity.screens.joinToString(",") { it.name })
            val intRow = preparedStatement.executeUpdate()

            if (intRow > 0) {
                entity.id = Database.getLastId( connection )
                return entity
            }


        }

return null
    }


    override fun delete(idEntity: Long, connection: Connection): Long? {


        connection.prepareStatement(DELETE_ROLE).use { preparedStatement ->
            preparedStatement.setLong(1, idEntity)
            val rowCount = preparedStatement.executeUpdate()
            if (rowCount > 0) {
                return idEntity
            }
        }
return null

    }

    override fun findById(iEntity: Long, connection: Connection): Role? {
        return null
    }

    override fun findAll(): List<Role> {

        val tempRole = mutableListOf<Role>()

        Database.connect().use { connection ->

            connection.createStatement().use { statement ->

                statement.executeQuery(SELECT_ALL_ROLE).use { resultSet ->

                    while (resultSet.next()) {
                        val role = Role()
                        role.id = resultSet.getLong("id")
                        role.name = resultSet.getString("name")
                        role.screens = resultSet.getString("screens").split(",").map {
                            ScreenEnum.valueOf(it)
                        }.toMutableList()

                        if (Context.currentEmployee.get() != null) {
                            val r = authorizeRole(role, Context.currentEmployee.get().role.screens)
                            if (r != null)
                                tempRole.add(r)
                        }
                    }


                }

            }

        }
        return tempRole

    }

    private fun authorizeRole(roleDb: Role, screensAuthorized: MutableList<ScreenEnum>): Role? {
        for (i in 0 until roleDb.screens.size) {
            if (!screensAuthorized.contains(roleDb.screens[i]))
                return null
        }
        return roleDb
    }


    private fun createQuerySelectRoleWith(screens: MutableList<ScreenEnum>): String {

        val builder = StringBuilder()
        builder.append("SELECT * FROM roles WHERE ")
        builder.append("INSTR( screens,'${screens[0]}' ) > 0 ")

        for (i in 1 until screens.size) {
            builder.append("AND INSTR( screens,'${screens[i]}' ) >0 ")
        }

        println(builder.toString())

        return builder.toString()
    }

    override fun update(entity: Role, connection: Connection): Role? {

        //connection.autoCommit = false

        connection.prepareStatement(UPDATE_ROLE).use { preparedStatement ->
            preparedStatement.setString(1, entity.name)
            preparedStatement.setString(2, entity.screens.joinToString(",") { it.name })
            preparedStatement.setLong(3, entity.id)
            val intRow = preparedStatement.executeUpdate()

            if (intRow > 0) {


                connection.commit()
                connection.autoCommit = true
                return entity


            }


        }
        return null

    }


}
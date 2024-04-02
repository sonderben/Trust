package com.sonderben.trust.db.dao

import Database
import Database.DATABASE_NAME
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

class RoleDao private constructor():CrudDao<Role> {
    companion object{
        @Volatile private var instence:RoleDao? = null
        fun getIntence():RoleDao{

            if (instence==null){
                synchronized(this){
                    if (instence==null){
                        instence = RoleDao()
                    }
                }
            }
            return instence!!
        }
        fun clearInstence(){
            instence = null
        }
    }


    val roles: ObservableList<Role> = FXCollections.observableArrayList()

    init {
        findAll()
    }




    override fun save(entity:Role):Completable{

        return Completable.create { emitter->
            Database.connect(DATABASE_NAME).use { connection ->

                connection.prepareStatement( INSERT_ROLE ).use { preparedStatement ->
                    preparedStatement.setString(1,entity.name)
                    preparedStatement.setString(2, entity.screens.joinToString(",") { it.name })
                    val intRow = preparedStatement.executeUpdate()

                    if ( intRow>0 ) {
                        entity.id = Database.getLastId()
                        roles.add(entity)
                        emitter.onComplete()
                    }else{
                        emitter.onError( Throwable("Can not be saved role: $entity") )
                    }


                }


            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }



    override fun delete(idEntity: Long): Completable {

        return Completable.create {emitter->
            Database.connect(DATABASE_NAME).use { connection ->
                connection.prepareStatement( DELETE_ROLE ).use { preparedStatement ->
                    preparedStatement.setLong(1,idEntity)
                    val rowCount = preparedStatement.executeUpdate()
                    if (rowCount>0){
                        roles.removeIf { it.id.equals(idEntity) }
                        emitter.onComplete()
                    }else
                        emitter.onError(Throwable("cant delete role, id: $idEntity"))
                }

            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())


    }

    override fun findById(iEntity: Long): Maybe<Role> {
        return Maybe.create {}
    }

    override fun findAll() {

        Single.create {emitter->

            Database.connect(DATABASE_NAME).use { connection ->
                connection.autoCommit = true
                connection.createStatement().use { statement ->

                    val  selectAuthorizeRole = createQuerySelectRoleWith( Context.currentEmployee.get().role.screens )

                    statement.executeQuery(SELECT_ALL_ROLE).use { resultSet ->

                        while ( resultSet.next() ){
                            val role = Role()
                            role.id = resultSet.getLong("id")
                            role.name = resultSet.getString("name")
                            role.screens = resultSet.getString("screens").split(",").map {
                                ScreenEnum.valueOf( it )
                            }.toMutableList()

                            val r = authorizeRole(role,Context.currentEmployee.get().role.screens)
                            if(r != null)
                                roles.add( r )
                        }
                        emitter.onSuccess( roles )

                    }

                }

            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )
            .subscribe ()

    }

    private fun authorizeRole(roleDb: Role, screensAuthorized: MutableList<ScreenEnum>): Role? {
        for ( i in 0 until roleDb.screens.size){
            if ( !screensAuthorized.contains(  roleDb.screens[i] ) )
                return null
        }
        return roleDb
    }

    /*private fun createQuerySelectRoleWith(screens: MutableList<ScreenEnum>): String{

        val builder = StringBuilder()
        builder.append("SELECT * FROM roles WHERE (screens LIKE '%${screens[0].name}%' ")

        if (screens.size>1){
            for (i in 1 until screens.size){
                builder.append(" OR screens LIKE '%${screens[i].name}%' ")
            }
        }
        builder.append(")")
        builder.append("and length(screens) <= ${screens.joinToString(",") { it.name }.length}")
        println(builder.toString())
        return builder.toString()
    }*/


    private fun createQuerySelectRoleWith(screens: MutableList<ScreenEnum>): String{

        val builder = StringBuilder()
        builder.append("SELECT * FROM roles WHERE ")
        builder.append("INSTR( screens,'${screens[0]}' ) > 0 ")

        for (i in 1 until screens.size){
            builder.append("AND INSTR( screens,'${screens[i]}' ) >0 ")
        }

        println(builder.toString())

        return builder.toString()
    }

    override fun update(entity: Role): Completable {
        return Completable.create { emitter->
            Database.connect(DATABASE_NAME).use { connection ->
                connection.autoCommit = false

                connection.prepareStatement( UPDATE_ROLE ).use { preparedStatement ->
                    preparedStatement.setString(1,entity.name)
                    preparedStatement.setString(2, entity.screens.joinToString(",") { it.name })
                    preparedStatement.setLong(3,entity.id)
                    val intRow = preparedStatement.executeUpdate()

                    if ( intRow>0 ) {


                        connection.commit()
                        connection.autoCommit = true
                        roles[ roles.indexOf( entity ) ] = entity
                        emitter.onComplete()

                    }else{
                        connection.rollback()
                        connection.autoCommit = true
                        emitter.onError( Throwable("Can not update role: $entity") )
                    }


                }


            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }



}
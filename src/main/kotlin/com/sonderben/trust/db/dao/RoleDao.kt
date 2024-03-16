package com.sonderben.trust.db.dao

import Database
import Database.DATABASE_NAME
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.DELETE_ROLE
import com.sonderben.trust.db.SqlDml.INSERT_ROLE
import com.sonderben.trust.db.SqlDml.INSERT_SCREEN
import com.sonderben.trust.db.SqlDml.SELECT_ALL_ROLE
import com.sonderben.trust.model.Role
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections

object RoleDao:CrudDao<Role> {
    val roles  = FXCollections.observableArrayList<Role>()

    init {
        findAll()
    }




    override fun save(entity:Role):Completable{
        return Completable.create { emitter->
            var tempRole = entity
            Database.connect(DATABASE_NAME).use { connection ->
                var roleId:Long

                connection.autoCommit = false

                connection.prepareStatement( INSERT_ROLE ).use { preparedStatement ->
                    preparedStatement.setString(1,entity.name)
                    val intRow = preparedStatement.executeUpdate()
                    if ( intRow>0 ){
                        roleId = Database.getLastId()
                        tempRole.id = roleId

                        for (screen in entity.screens){
                            connection.prepareStatement( INSERT_SCREEN ).use { ps ->
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
                        connection.autoCommit = true
                        roles.add( tempRole )
                        emitter.onComplete()

                    }else{
                        connection.rollback()
                        connection.autoCommit = true
                        emitter.onError( Throwable("Can not be saved role: $entity") )
                    }
                }


            }
        }.subscribeOn(Schedulers.io())
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
                    }
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


                    statement.executeQuery(SELECT_ALL_ROLE).use { resultSet ->

                        while ( resultSet.next() ){
                            val role = Role()
                            role.id = resultSet.getLong("id")
                            role.name = resultSet.getString("name")
                            role.screens = Database.findScreenByIdRole(role.id)

                            roles.add( role )
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

    override fun update(entity: Role): Completable {
        return Completable.create {  }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    private fun deleteById(){}


}
package com.sonderben.trust.db.dao

import Database
import Database.DATABASE_NAME
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml
import entity.CategoryEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import java.sql.SQLException


object CategoryDao : CrudDao<CategoryEntity>{
     val categories  = FXCollections.observableArrayList<CategoryEntity>()

    init {
        findAll()
    }

    override fun save(entity:CategoryEntity): Completable {
        return  Completable.create { emitter->
            Database.connect(DATABASE_NAME).use { connection ->
                connection.prepareStatement( SqlDml.insertCategory ).use { preparedStatement ->
                    preparedStatement.apply {
                        setString(1,entity.code)
                        setString(2,entity.description)
                        setDouble(3,entity.discount)
                    }
                    if ( preparedStatement.executeUpdate()>0){
                        entity.id = Database.getLastId()
                        categories.add(entity)
                        emitter.onComplete()
                    }else{
                        emitter.onError(Throwable("can not save category: $entity"))
                    }

                }

            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )
    }

    override fun findAll() {


         Single.create{emitter->
            Database.connect(DATABASE_NAME).use { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery(SqlDml.selectAllCategory).use { resultSet ->
                        while (resultSet.next()) {

                            val category = CategoryEntity()
                            category.apply {
                                id = resultSet.getLong("id")
                                code = resultSet.getString("code")
                                description = resultSet.getString("description")
                                discount = resultSet.getDouble("discount")
                            }

                            categories.add(category)
                        }
                        emitter.onSuccess(categories)
                    }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )
             .subscribe()


    }

    override fun update(entity: CategoryEntity): Completable {

        return Completable.create { emitter->
            Database.connect("").use { connection ->
                connection.prepareStatement(SqlDml.updateCategory).use { preparedStatement ->

                    preparedStatement.setString(1,entity.code)
                    preparedStatement.setString(2,entity.description)
                    preparedStatement.setDouble(3,entity.discount)
                    preparedStatement.setLong(4,entity.id)

                    if ( preparedStatement.executeUpdate() > 0 ){
                        categories[ categories.indexOf( entity ) ] = entity
                        println("prueba : ${entity.id}")
                        emitter.onComplete()
                    }
                    else{
                        emitter.onError( Throwable("can not update category: $entity") )
                        println("prueba false : ${entity.id}")
                    }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )
    }

    override fun delete(idEntity:Long): Completable {
        return Completable.create { emitter ->
            Database.connect(DATABASE_NAME).use { connection ->
                connection.prepareStatement(SqlDml.deleteCategory).use { statement ->
                    statement.setLong(1,idEntity)
                    val res = statement.executeUpdate()
                    if (res>0){
                        categories.removeIf {
                            it.id == idEntity
                        }
                        emitter.onComplete()
                    }else{
                        emitter.onError( Throwable("can not delete category with id: $idEntity") )
                    }
                }
            }

        }.subscribeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )

    }

    override fun findById(iEntity: Long): Maybe<CategoryEntity> {
        return Maybe.create {  }
    }


}
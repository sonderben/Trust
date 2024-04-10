package com.sonderben.trust.db.dao

import Database

import com.sonderben.trust.db.SqlDml
import entity.CategoryEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import java.sql.Connection


class CategoryDao : CrudDao<CategoryEntity>{


    override fun save(entity:CategoryEntity,connection:Connection):CategoryEntity? {


                connection.prepareStatement( SqlDml.insertCategory ).use { preparedStatement ->
                    preparedStatement.apply {
                        setString(1,entity.code)
                        setString(2,entity.description)
                        setDouble(3,entity.discount)
                    }
                    if ( preparedStatement.executeUpdate()>0){
                        entity.id = Database.getLastId( connection )
                        return entity
                    }

                }

           return null

    }

    override fun findAll():List<CategoryEntity> {
        val tempCategories = mutableListOf<CategoryEntity>()


                Database.connect().use { connection ->
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

                                tempCategories.add(category)
                            }

                        }
                    }
                }

        return tempCategories




    }

    override fun update(entity: CategoryEntity,connection:Connection): CategoryEntity? {



                connection.prepareStatement(SqlDml.updateCategory).use { preparedStatement ->

                    preparedStatement.setString(1,entity.code)
                    preparedStatement.setString(2,entity.description)
                    preparedStatement.setDouble(3,entity.discount)
                    preparedStatement.setLong(4,entity.id)

                    if ( preparedStatement.executeUpdate() > 0 ){
                       return entity
                    }

                }
        return null


    }

    override fun delete(idEntity:Long,connection: Connection): Long? {


                connection.prepareStatement(SqlDml.deleteCategory).use { statement ->
                    statement.setLong(1,idEntity)
                    val res = statement.executeUpdate()
                    if (res>0){
                        return idEntity

                    }
                }
        return null


    }

    override fun findById(iEntity: Long, connection: Connection): CategoryEntity? {
        return null
    }


}
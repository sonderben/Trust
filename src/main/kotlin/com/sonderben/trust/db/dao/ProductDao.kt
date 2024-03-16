package com.sonderben.trust.db.dao

import Database.DATABASE_NAME
import com.sonderben.trust.Context
import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.FIND_PRODUCT_EXPIRED
import com.sonderben.trust.db.SqlDml.SELECT_ALL_PRODUCT
import com.sonderben.trust.db.SqlDml.SELECT_BESTSELLER_GROUP_BY_CODE
import com.sonderben.trust.db.SqlDml.SELECT_PRODUCT_BY_CODE
import com.sonderben.trust.db.SqlDml.UPDATE_PRODUCT
import com.sonderben.trust.db.SqlDml.UPDATE_QUANTITY_PRODUCT_REMAININHG
import com.sonderben.trust.toTimestamp
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.sql.Statement
import java.sql.Timestamp
import java.util.Calendar

object ProductDao : CrudDao<ProductEntity> {
    var products: ObservableList<ProductEntity> = FXCollections.observableArrayList()

    init {
        findAll()
    }

    override fun save(entity: ProductEntity): Completable {

        return  Completable.create { emitter->
            var insertProduct = """
            INSERT INTO ${SqlDdl.products} 
            (discount, itbis, purchasePrice, quantity, sellingPrice,id_category, dateAdded, id_employee, expirationDate, code, description,quantityRemaining,sellby ) 
            values(?,?,?,?,?,?,?,?,?,?,?,?,?)
        """.trimIndent()
            Database.connect(DATABASE_NAME).use { connection ->
                connection.prepareStatement(insertProduct).use { preparedStatement ->
                    preparedStatement.setDouble(1, entity.discount)
                    preparedStatement.setDouble(2, entity.itbis)
                    preparedStatement.setDouble(3, entity.purchasePrice)
                    preparedStatement.setFloat(4, entity.quantity)
                    preparedStatement.setDouble(5, entity.sellingPrice)
                    preparedStatement.setLong(6, entity.category.id)
                    preparedStatement.setTimestamp(7, Timestamp(Calendar.getInstance().timeInMillis))
                    preparedStatement.setLong(8, entity.employee.id)
                    preparedStatement.setTimestamp(9, Timestamp(entity.expirationDate.timeInMillis))
                    val tempCode = entity.code.padStart(12, '0')
                    preparedStatement.setString(10, tempCode)
                    preparedStatement.setString(11, entity.description)
                    preparedStatement.setFloat(12, entity.quantity)
                    preparedStatement.setString(13,entity.sellBy)
                    val rowCount = preparedStatement.executeUpdate()
                    val lastId = Database.getLastId()
                    entity.id = lastId



                    if (rowCount > 0 && lastId != null) {
                        products.add(entity)
                        emitter.onComplete()
                    } else {
                        println("rowCount: $rowCount , last id: $lastId")
                        emitter.onError( Throwable("Can not save product: $entity") )
                    }



                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())

    }

    override fun delete(idEntity: Long): Completable {
        return Completable.create { emitter->
            val deleteProduct = "delete from ${SqlDdl.products} where id = ?"
            Database.connect("").use { connection ->
                connection.prepareStatement( deleteProduct ).use { preparedStatement ->
                    preparedStatement.setLong(1,idEntity)
                    val rowCount = preparedStatement.executeUpdate()
                    if (rowCount>0){
                        emitter.onComplete()
                        products.removeIf{it.id == idEntity}
                    }else {
                        emitter.onError( Throwable( "can not delete product of id: $idEntity" ) )
                    }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    override fun findById(iEntity: Long): Maybe<ProductEntity> {
        return Maybe.create{}
    }

    override fun findAll() {


        Single.create { emitter->
            Database.connect(DATABASE_NAME).use { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery( SELECT_ALL_PRODUCT ).use { resultSet ->
                        while (resultSet.next()) {
                            val employee = EmployeeEntity()
                            employee.apply {
                                id = resultSet.getLong("id_employee")
                                userName = resultSet.getString("userName")
                            }
                            val category = CategoryEntity()
                            category.apply {
                                id = resultSet.getLong("id_category")
                                code = resultSet.getString("code_categpry")
                                description = resultSet.getString("description_category")
                                discount = resultSet.getDouble("discount_category")
                            }
                            val prod = ProductEntity(
                                resultSet.getString("code_product"),
                                resultSet.getString("sellby"),
                                resultSet.getString("description_product"),
                                resultSet.getDouble("sellingPrice"),
                                resultSet.getDouble("purchaseprice"),
                                resultSet.getDouble("discount_product"),
                                resultSet.getDouble("itbis"),
                                resultSet.getFloat("quantity"),
                                resultSet.getFloat("quantityRemaining"),
                                Util.timeStampToCalendar(resultSet.getTimestamp("dateAdded")),
                                Util.timeStampToCalendar(resultSet.getTimestamp("expirationDate")),
                                category,
                                employee
                            )
                            prod.id = resultSet.getLong("id")
                            products.add( prod )

                        }
                        emitter.onSuccess( products )
                    }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )
            .subscribe ()




    }

    override fun update(entity: ProductEntity): Completable {
        if(entity.id == null)
            throw RuntimeException(" id product can not be null")

        return Completable.create { emitter->
            Database.connect("").use { connection ->
                connection.prepareStatement( UPDATE_PRODUCT ).use { preparedStatement ->
                    preparedStatement.setDouble(1,entity.discount)
                    preparedStatement.setDouble(2,entity.itbis)
                    preparedStatement.setDouble(3,entity.purchasePrice)
                    preparedStatement.setFloat(4,entity.quantity)
                    preparedStatement.setDouble(5,entity.sellingPrice)
                    preparedStatement.setLong(6,entity.category.id)
                    preparedStatement.setLong(7,Context.currentEmployee.value.id)
                    preparedStatement.setTimestamp(8,entity.expirationDate.toTimestamp())
                    preparedStatement.setString(9,entity.code)
                    preparedStatement.setString(10,entity.description)
                    preparedStatement.setFloat(11,entity.quantityRemaining)
                    preparedStatement.setString(12,entity.sellBy)
                    preparedStatement.setLong(13,entity.id)

                    val rowCount = preparedStatement.executeUpdate()


                    if (rowCount>0 || rowCount == Statement.SUCCESS_NO_INFO  ) {
                        products[ products.indexOf( entity ) ] = entity
                        emitter.onComplete()
                    }else{
                        emitter.onError( Throwable( "Can not update product: $entity" ) )
                    }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }




}


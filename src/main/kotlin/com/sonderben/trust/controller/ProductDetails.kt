package com.sonderben.trust.controller

import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlDml
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler

object ProductDetails {
    fun findProductByCode(code: String): Maybe<ProductEntity> {
        return Maybe.create<ProductEntity?> { emitter->
            Database.connect("DATABASE_NAME").use { connection ->
                connection.prepareStatement(SqlDml.SELECT_PRODUCT_BY_CODE).use { preparedStatement ->
                    preparedStatement.setString(1, code.padStart(12, '0'))
                    val resultSet = preparedStatement.executeQuery()

                    //
                    val employee = EmployeeEntity()
                    employee.id = resultSet.getLong("id_employee")

                    val category = CategoryEntity()
                    category.apply {
                        id = resultSet.getLong("id_category")
                        this.code = resultSet.getString("code_categpry")
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



                    if (prod.code == null || prod.code.isBlank()) {
                        emitter.onComplete()
                    }else{
                        emitter.onSuccess(prod)
                    }

                }


            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())


    }


    fun findProductsExpired(): MutableList<ProductEntity> {
        val tempProduct = mutableListOf<ProductEntity>()

        Database.connect("DATABASE_NAME").use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(SqlDml.FIND_PRODUCT_EXPIRED).use { resultSet ->
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
                        tempProduct.add(prod)
                    }
                    return tempProduct
                }
            }
        }


    }


    fun updateQuantityRemaining(codeProduct: String, qtyBought: Float): Int {

        val connection = Database.connect("DATABASE_NAME")

        connection.prepareStatement(SqlDml.UPDATE_QUANTITY_PRODUCT_REMAININHG).use { preparedStatement ->

            preparedStatement.setFloat(1, qtyBought)
            preparedStatement.setString(2, codeProduct)

            return preparedStatement.executeUpdate()


        }


    }

    fun bestSellers(qty:Float,frequency:Int,benefit:Float):List<BestSeller>{
        val bestSellers:MutableList<BestSeller> = mutableListOf()


        Database.connect("").use { connection ->
            connection.prepareStatement(SqlDml.SELECT_BESTSELLER_GROUP_BY_CODE).use { preparedStatement ->
                preparedStatement.setInt(1,frequency)
                preparedStatement.setFloat(2,benefit)
                preparedStatement.setFloat(3,qty)

                preparedStatement.executeQuery().use { resultSet ->
                    while (resultSet.next()){
                        println(resultSet.getDouble("points"))
                        bestSellers.add(
                            BestSeller(
                                resultSet.getString("code"),
                                resultSet.getString("description"),
                                resultSet.getFloat("quantity"),
                                resultSet.getInt("frequency"),
                                resultSet.getDouble("benefit"),
                                resultSet.getDouble("points"))
                        )
                    }
                    return bestSellers
                }
            }
        }
    }

    data class BestSeller (
        val code:String,
        val description:String,
        val quantity:Float,
        val frequency:Int,
        val benefit: Double,
        val points:Double)
}
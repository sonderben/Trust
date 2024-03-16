package com.sonderben.trust.db.dao

import Database
import Database.DATABASE_NAME
import com.sonderben.trust.controller.ProductDetails
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.FIND_BY_INVOICE_CODE
import com.sonderben.trust.db.SqlDml.INSERT_PRODUCT_RETURN
import com.sonderben.trust.db.SqlDml.PRODUCT_SOLD_BY_CODE
import com.sonderben.trust.db.SqlDml.SELECT_PRODUCT_RETURNED
import com.sonderben.trust.db.SqlDml.UPDATE_STATUS_PRODUCT_IS_RETURNED
import com.sonderben.trust.toCalendar
import com.sonderben.trust.toTimestamp
import entity.InvoiceEntity
import entity.ProductSaled
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import java.sql.SQLException
import java.sql.Timestamp
import java.util.Calendar
import kotlin.collections.List

object InvoiceDao:CrudDao<InvoiceEntity> {
    //var invoices = FXCollections.observableArrayList<InvoiceEntity>()



    override fun save(entity: InvoiceEntity): Completable {
        return Completable.create { emitter->
            val insertInvoice = "insert into ${SqlDdl.invoices} (dateCreated, codeBar, id_employee, id_customer) values (?,?,?,?)"

            val insertProductSealed = "Insert into ${SqlDdl.productSealed}" +
                    " (code,description,price,quantity,discount,itbis,total,wasDiscountCategory,category,expirationDate,sellby)  values(?,?,?,?,?,?,?,?,?,?,?);"

            val insertInvoiceProductSealed = "Insert into  ${SqlDdl.invoiceProductSealed} (id_invoice,id_product_sealed) values(?,?);"


            Database.connect(DATABASE_NAME).use { connection ->
                connection.autoCommit = false
                var idInvoice: Long
                connection.prepareStatement(insertInvoice).use { statement ->
                    statement.setTimestamp(1,Timestamp(entity.date.timeInMillis))
                    statement.setString(2,entity.codeBar)
                    statement.setLong(3,entity.employee.id)
                    entity.customer?.id?.let {
                        statement.setLong(4,it)
                    }
                    val rowCount = statement.executeUpdate()

                    if (rowCount<=0){
                        println("error al insert Invoice")
                        connection.rollback()
                        connection.autoCommit = false
                        emitter.onError( Throwable("can not save invoice: $entity") )
                    }


                    idInvoice = Database.getLastId()
                    connection.prepareStatement(insertProductSealed).use { preparedStatement ->

                        val productSales:List<ProductSaled> = entity.products
                        for (productSale in productSales){
                            preparedStatement.setString(1,productSale.code)
                            preparedStatement.setString(2,productSale.description)
                            preparedStatement.setFloat(3,productSale.price)
                            preparedStatement.setFloat(4,productSale.qty)
                            preparedStatement.setFloat(5,productSale.discount)
                            preparedStatement.setFloat(6,productSale.itbis)
                            preparedStatement.setDouble(7,productSale.total)
                            preparedStatement.setBoolean(8,productSale.isWasDiscountCategory)
                            preparedStatement.setString(9,productSale.category)
                            preparedStatement.setTimestamp(10,productSale.expirationDate.toTimestamp())
                            preparedStatement.setString(11,productSale.sellBy)

                            val rc = preparedStatement.executeUpdate()
                            if (rc>0){
                                val r = ProductDetails.updateQuantityRemaining(productSale.code,productSale.qty)

                                if (r<=0){
                                    connection.rollback()
                                    connection.autoCommit = true
                                    throw SQLException("can not update qty remaining product")
                                }
                            }

                            if (rc<=0){
                                println("error al insert ProductSealed")
                                connection.rollback()
                                connection.autoCommit = true
                                emitter.onError( Throwable("error al insert ProductSealed") )
                            }

                            val idProduct = Database.getLastId()

                            connection.prepareStatement(insertInvoiceProductSealed).use { ps ->
                                ps.setLong(1,idInvoice)
                                ps.setLong(2,idProduct)
                                val rowCount2 = ps.executeUpdate()

                                if (rowCount2<=0){
                                    connection.rollback()
                                    println("error al insert InvoiceProductSealed")
                                    connection.autoCommit = true
                                    emitter.onError( Throwable("error al insert InvoiceProductSealed") )
                                }
                            }
                        }



                        connection.commit()
                        emitter.onComplete()

                    }


                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())



    }

    override fun delete(idEntity: Long): Completable {
        return Completable.create {

        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
            .observeOn(JavaFxScheduler.platform())
    }

    override fun findById(iEntity: Long): Maybe<InvoiceEntity> {
        return Maybe.create {  }
    }

    override fun findAll() {

    }

    override fun update(entity: InvoiceEntity): Completable {
        return Completable.create {  }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    fun productSealed():List<ProductSealed>{
        val productsSealed = mutableListOf<ProductSealed>()

       Database.connect(DATABASE_NAME).use { connection ->
           connection.createStatement().use { statement ->
               statement.executeQuery(PRODUCT_SOLD_BY_CODE).use { resultSet ->
                   while (resultSet.next()){



                       productsSealed.add( ProductSealed(
                           resultSet.getString("code"),
                                   resultSet.getString("description"),
                                   resultSet.getString("total_price"),
                                   resultSet.getString("total_quantity"),
                                   resultSet.getString("dateCreated"),
                                   resultSet.getString("category")
                       ) )
                   }
                   return productsSealed
               }

           }
       }
    }

    fun findByInvoiceCode(invoiceCode: String):List<ProductToReturned> {
        val list = mutableListOf<ProductToReturned>()

        Database.connect("").use { connection ->
            connection.prepareStatement( FIND_BY_INVOICE_CODE ).use { preparedStatement ->
                preparedStatement.setString(1,invoiceCode)
                preparedStatement.executeQuery().use { resultSet ->
                    while(resultSet.next()){
                        val productToReturned = ProductToReturned(
                            resultSet.getString("code"),
                            resultSet.getString("description"),
                            resultSet.getDouble("price"),
                            resultSet.getFloat("quantity"),
                            resultSet.getDouble("total_price"),
                            resultSet.getString("category"),
                            resultSet.getTimestamp("dateCreated").toCalendar(),
                            resultSet.getLong("productSoldId"),
                            resultSet.getLong("invoicesId"),
                            resultSet.getBoolean("isReturned")
                        )
                        list.add( productToReturned )
                    }
                    return list;
                }
            }
        }
    }

    fun saveProductReturned(idInvoice:Long, idProductSealed:List<Long>, idEmployee:Long, reason:String, action:String):Boolean{

        Database.connect("").use { connection ->
            connection.autoCommit = false
            connection.prepareStatement( INSERT_PRODUCT_RETURN ).use { preparedStatement ->
                preparedStatement.setLong(1,idInvoice)
                preparedStatement.setLong(2,idEmployee)
                preparedStatement.setString(3,reason)
                preparedStatement.setString(4,action)
                val rowCount = preparedStatement.executeUpdate()

                for (id in idProductSealed){
                    productSoldReturned(id=id)
                }
                if (rowCount>0){
                    connection.commit()
                }else{
                    connection.rollback()
                }
                connection.autoCommit = false
                return rowCount>0
            }
        }
    }
    fun getProductReturned():List<ProductReturned>{

        val productsReturned = mutableListOf<ProductReturned>()
        Database.connect("").use { connection ->
            connection.prepareStatement( SELECT_PRODUCT_RETURNED ).use { preparedStatement ->
                preparedStatement.executeQuery().use { resultSet ->
                    while (resultSet.next()){
                        val pr = ProductReturned(
                            resultSet.getString("productCode"),
                            resultSet.getString("description"),
                            resultSet.getFloat("quantity"),
                            resultSet.getDouble("total"),
                            resultSet.getTimestamp("dateReturned").toCalendar(),
                            resultSet.getTimestamp("dateBought").toCalendar(),
                            resultSet.getString("reason"),
                            resultSet.getString("action"),
                            resultSet.getTimestamp("expirationDate").toCalendar(),
                            resultSet.getString("customerCode"),
                            resultSet.getString("employee"),
                            resultSet.getString("invoice_codebar")
                        )
                        productsReturned.add(pr)
                    }
                    return productsReturned
                }
            }
        }
    }

    private fun productSoldReturned(id: Long) {


        Database.connect("").prepareStatement( UPDATE_STATUS_PRODUCT_IS_RETURNED ).use { preparedStatement ->
            preparedStatement.setLong(1,id)
           val rowCount =  preparedStatement.executeUpdate()
            if (rowCount<=0)
                throw SQLException("can not set isReturned in ${SqlDdl.productSealed} table")
        }

    }


    data class ProductSealed(var code:String,
             var description:String,
             var totalPrice:String,
             var totalQuantity:String,
             var dateCreated:String,
             var category:String){

    }


    data class ProductToReturned(
        var code:String,
        var description:String,
        var price:Double,
        var quantity:Float,
        var totalPrice:Double,
        var category:String,
        var dateCreated:Calendar,
        var productSoldId:Long,
        var invoiceId:Long,
        var isReturned:Boolean){

    }
    /*
    SELECT ,, ,,, ,
 ,,ps.expirationDate

     */
    data class ProductReturned(
        var productCode:String,
        var description:String,
        var quantity:Float,
        var totalPrice:Double,
        var dateReturned:Calendar,
        var dateBought:Calendar,
        var reson:String,
        var action:String,
        var expirationDate:Calendar,
        var customerCode:String,
        var employee:String,
        var invoiceCodeBar:String)

}
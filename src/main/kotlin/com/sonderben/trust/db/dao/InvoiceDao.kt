package com.sonderben.trust.db.dao

import Database
import com.sonderben.trust.db.SqlCreateTables
import entity.InvoiceEntity
import entity.ProductSaled
import javafx.collections.FXCollections
import java.sql.Timestamp
import kotlin.collections.List

object InvoiceDao:CrudDao<InvoiceEntity> {
    var invoices = FXCollections.observableArrayList<InvoiceEntity>()



    override fun save(entity: InvoiceEntity): Boolean {

        val insertInvoice = "insert into ${SqlCreateTables.invoices} (dateCreated, codeBar, id_employee, id_customer) values (?,?,?,?)"

        val insertProductSealed = "Insert into ${SqlCreateTables.productSealed}" +
                " (code,description,price,quantity,discount,itbis,total,wasDiscountCategory,category)  values(?,?,?,?,?,?,?,?,?);"

        val insertInvoiceProductSealed = "Insert into  ${SqlCreateTables.invoiceProduct} (id_invoice,id_product_sealed) values(?,?);"


        Database.connect().use { connection ->
            connection.autoCommit = false
            var idInvoice = 0L
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
                    return false
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
                        val rowCount = preparedStatement.executeUpdate()

                        if (rowCount<=0){
                            println("error al insert ProductSealed")
                            connection.rollback()
                            connection.autoCommit = true
                            return false
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
                                return false
                            }
                        }
                    }

                    connection.commit()

                }


            }




        }
        return true
    }

    override fun delete(idEntity: Long): Boolean {
        return true
    }

    override fun findById(iEntity: Long): InvoiceEntity? {
        return null
    }

    override fun findAll(): Boolean {
        return true
    }

    override fun update(entity: InvoiceEntity): Boolean {
        return true
    }

    fun productSealed():List<ProductSealed>{
        val productsSealed = mutableListOf<ProductSealed>()
        val ps = """
            SELECT ps.code,
            ps.description,
            SUM(ps.price * ps.quantity) as total_price,
            SUM(ps.quantity) as total_quantity,
            ps.category,
            ip.id_invoice,
            iv.dateCreated
            FROM productSealed as ps
            INNER JOIN invoiceProduct as ip ON ip.id_product_sealed = ps.id
            INNER JOIN invoices as iv ON iv.id = ip.id_invoice
            INNER JOIN Employee as emp ON emp.id = iv.id_employee
            /*WHERE Date(iv.dateCreated/1000,'unixepoch') = date('now')*/
            GROUP BY ps.code;

        """.trimIndent()
       Database.connect().use { connection ->
           connection.createStatement().use { statement ->
               statement.executeQuery(ps).use { resultSet ->
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
//select ps.code ,ps.description,ps.price,ps.quantity,ip.id_invoice,iv.dateCreated   from productSealed as ps
//            INNER JOIN invoiceProduct as ip on
//            ip.id_product_sealed = ps.id
//            INNER JOIN  invoices as iv on
//            iv.id =  ip.id_invoice
//            INNER JOIN Employee as emp on
//            emp.id = iv.id_employee
//            WHERE Date(iv.dateCreated/1000,'unixepoch') = date('now')

    data class ProductSealed(var code:String,
             var description:String,
             var totalPrice:String,
             var totalQuantity:String,
             var dateCreated:String,
             var category:String){

    }

}
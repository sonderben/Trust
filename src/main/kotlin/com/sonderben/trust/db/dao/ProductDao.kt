package com.sonderben.trust.db.dao

import Database.DATABASE_NAME
import com.sonderben.trust.Context
import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import com.sonderben.trust.toTimestamp
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import java.sql.Timestamp
import java.util.Calendar

object ProductDao : CrudDao<ProductEntity> {
    var products = FXCollections.observableArrayList<ProductEntity>()

    init {
        findAll()
    }

    override fun save(entity: ProductEntity): Completable {

        return  Completable.create { emitter->
            var insertProduct = """
            INSERT INTO ${SqlCreateTables.products} 
            (discount, itbis, purchasePrice, quantity, sellingPrice,id_category, dateAdded, id_employee, expirationDate, code, description,quantityRemaining,sellby ) 
            values(?,?,?,?,?,?,?,?,?,?,?,?,?)
        """.trimIndent()
            Database.connect(DATABASE_NAME).use { connection ->
                connection.prepareStatement(insertProduct).use { preparedStatement ->
                    preparedStatement.setDouble(1, entity.discount)
                    preparedStatement.setDouble(2, entity.itbis)
                    preparedStatement.setDouble(3, entity.purchasePrice)
                    preparedStatement.setInt(4, entity.quantity)
                    preparedStatement.setDouble(5, entity.sellingPrice)
                    preparedStatement.setLong(6, entity.category.id)
                    preparedStatement.setTimestamp(7, Timestamp(Calendar.getInstance().timeInMillis))
                    preparedStatement.setLong(8, entity.employee.id)
                    preparedStatement.setTimestamp(9, Timestamp(entity.expirationDate.timeInMillis))
                    val tempCode = entity.code.padStart(12, '0')
                    preparedStatement.setString(10, tempCode)
                    preparedStatement.setString(11, entity.description)
                    preparedStatement.setInt(12, entity.quantity)
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
            val deleteProduct = "delete from ${SqlCreateTables.products} where id = ?"
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

    override fun findAll(): Boolean {
        val tempProduct = mutableListOf<ProductEntity>()
        val selectAll = """
            SELECT products.quantityRemaining,products.sellby,products.id,products.discount as discount_product,quantity,itbis,sellingPrice,purchaseprice,
            id_category,products.dateAdded,id_employee,expirationDate,products.code as code_product,products.description as description_product,
            Categories.discount as discount_category,Categories.code as code_categpry,Categories.description as description_category,userName
             from ${SqlCreateTables.products} INNER JOIN Employee  on 
            products.id_employee = Employee.id
            INNER JOIN Categories on 
            products.id_category = Categories.id
        """.trimIndent()
        Database.connect(DATABASE_NAME).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(selectAll).use { resultSet ->
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
                            resultSet.getInt("quantity"),
                            resultSet.getInt("quantityRemaining"),
                            Util.timeStampToCalendar(resultSet.getTimestamp("dateAdded")),
                            Util.timeStampToCalendar(resultSet.getTimestamp("expirationDate")),
                            category,
                            employee
                        )
                        prod.id = resultSet.getLong("id")
                        tempProduct.add(prod)
                    }
                    products.addAll(tempProduct)
                }
            }
        }



        return false
    }

    override fun update(entity: ProductEntity): Completable {
        if(entity.id == null)
            throw RuntimeException(" id product can not be null")
        val update = """update  ${SqlCreateTables.products} 
            set discount = ?, 
            itbis = ?, 
            purchasePrice = ?, 
            quantity = ?, 
            sellingPrice = ?,
            id_category = ?,  
            id_employee = ?, 
            expirationDate = ?, 
            code = ?, 
            description = ?,
            quantityRemaining = ? ,
            sellby = ?
            where id = ?;""".trimMargin()
        return Completable.create { emitter->
            Database.connect("").use { connection ->
                connection.prepareStatement( update ).use { preparedStatement ->
                    preparedStatement.setDouble(1,entity.discount)
                    preparedStatement.setDouble(2,entity.itbis)
                    preparedStatement.setDouble(3,entity.purchasePrice)
                    preparedStatement.setInt(4,entity.quantity)
                    preparedStatement.setDouble(5,entity.sellingPrice)
                    preparedStatement.setLong(6,entity.category.id)
                    preparedStatement.setLong(7,Context.currentEmployee.value.id)
                    preparedStatement.setTimestamp(8,entity.expirationDate.toTimestamp())
                    preparedStatement.setString(9,entity.code)
                    preparedStatement.setString(10,entity.description)
                    preparedStatement.setInt(11,entity.quantityRemaining)
                    preparedStatement.setLong(12,entity.id)
                    preparedStatement.setString(13,entity.sellBy)

                    val rowCount = preparedStatement.executeUpdate()

                    if (rowCount>0 ) {
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

    fun findProductByCode(code: String): ProductEntity? {

        val selectByCode = """
            SELECT  products.id,products.sellby,products.quantityRemaining,products.discount as discount_product,quantity,itbis,sellingPrice,purchaseprice,
            id_category,products.dateAdded,id_employee,expirationDate,products.code as code_product,products.description as description_product,
            Categories.discount as discount_category,Categories.code as code_categpry,Categories.description as description_category
             from products 
            INNER JOIN Categories on 
            products.id_category = Categories.id
            where code_product = ?
        """.trimIndent()


        Database.connect(DATABASE_NAME).use { connection ->
            connection.prepareStatement(selectByCode).use { preparedStatement ->
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
                    resultSet.getInt("quantity"),
                    resultSet.getInt("quantityRemaining"),
                    Util.timeStampToCalendar(resultSet.getTimestamp("dateAdded")),
                    Util.timeStampToCalendar(resultSet.getTimestamp("expirationDate")),
                    category,
                    employee
                )
                prod.id = resultSet.getLong("id")

                if (prod.code == null || prod.code.isBlank()) {
                    return null
                }
                return prod
            }
            //

        }

    }


    fun findProductsExpired(): MutableList<ProductEntity> {
        val tempProduct = mutableListOf<ProductEntity>()
        val selectAll = """
            SELECT products.quantityRemaining,products.id,products.discount as discount_product,quantity,itbis,sellingPrice,purchaseprice,
            id_category,products.dateAdded,id_employee,expirationDate,products.code as code_product,products.description as description_product,
            Categories.discount as discount_category,Categories.code as code_categpry,Categories.description as description_category,userName
             from ${SqlCreateTables.products} INNER JOIN Employee  on 
            products.id_employee = Employee.id
            INNER JOIN Categories on 
            products.id_category = Categories.id
            WHERE products.quantityRemaining > 0 and Date(expirationDate/1000,'unixepoch') <= date('now')
        """.trimIndent()
        Database.connect(DATABASE_NAME).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(selectAll).use { resultSet ->
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
                            resultSet.getInt("quantity"),
                            resultSet.getInt("quantityRemaining"),
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
        val updateEmployee =
            "update  ${SqlCreateTables.products} set quantityRemaining = quantityRemaining - ? where id = ?"

        val connection = Database.connect(DATABASE_NAME)

        connection.prepareStatement(updateEmployee).use { preparedStatement ->

            preparedStatement.setFloat(1, qtyBought)
            preparedStatement.setString(2, codeProduct)

            return preparedStatement.executeUpdate()


        }


    }

    fun bestSellers(qty:Float,frequency:Int,benefit:Float):List<BestSeller>{
        val bestSellers:MutableList<BestSeller> = mutableListOf()
        val selectBestSeller = """
            SELECT code,description,quantity,frequency  ,benefit,( (frequency * ?) + (benefit * ?) + (quantity * ?) ) as points  from
	            (SELECT ps.code,ps.description,ps.quantity,count(ps.code) as frequency,( (sellingPrice-purchasePrice)*ps.quantity ) as benefit from 
                    ${SqlCreateTables.productSealed} as ps 
	                INNER JOIN ${SqlCreateTables.products} as p on ps.code = p.code
	            GROUP by ps.code) ORDER by points DESC LIMIT 100
        """.trimIndent()

        Database.connect("").use { connection ->
            connection.prepareStatement(selectBestSeller).use { preparedStatement ->
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

    /*
    code,description,quantity,frequency  ,benefit,(?*3 + ?*2 + ?*2) as points
     */


    data class BestSeller (
        val code:String,
        val description:String,
        val quantity:Float,
        val frequency:Int,
        val benefit: Double,
        val points:Double)


}



















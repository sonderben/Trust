package com.sonderben.trust.db.dao

import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import javafx.collections.FXCollections
import java.sql.Timestamp
import java.util.Calendar

object ProductDao:CrudDao<ProductEntity> {
    var products = FXCollections.observableArrayList<ProductEntity>()

    init {
        findAll()
    }
    override fun save(entity: ProductEntity): Boolean {

        var insertProduct = """
            INSERT INTO ${SqlCreateTables.products} 
            (discount, itbis, purchasePrice, quantity, sellingPrice,id_category, dateAdded, id_employee, expirationDate, code, description,quantityRemaining ) 
            values(?,?,?,?,?,?,?,?,?,?,?,?)
        """.trimIndent()
        Database.connect().use { connection ->
            connection.prepareStatement(insertProduct).use {preparedStatement ->
                preparedStatement.setDouble(1,entity.discount)
                preparedStatement.setDouble(2,entity.itbis)
                preparedStatement.setDouble(3,entity.purchasePrice)
                preparedStatement.setInt(4,entity.quantity)
                preparedStatement.setDouble(5,entity.sellingPrice)
                preparedStatement.setLong(6,entity.category.id)
                preparedStatement.setTimestamp(7,Timestamp(Calendar.getInstance().timeInMillis))
                preparedStatement.setLong(8,entity.employee.id)
                preparedStatement.setTimestamp(9,Timestamp(entity.expirationDate.timeInMillis) )
                val tempCode = entity.code.padStart(12,'0')
                preparedStatement.setString(10,tempCode)
                preparedStatement.setString(11,entity.description)
                preparedStatement.setInt(12,entity.quantity)
                val rowCount = preparedStatement.executeUpdate()
                val lastId = Database.getLastId()
                entity.id = lastId



                if (rowCount>0 && lastId != null){
                    products.add( entity )
                    return true
                }else{
                    println("rowCount: $rowCount , last id: $lastId")
                }
                return false


            }
        }

    }

    override fun delete(idEntity: Long): Boolean {
        return false
    }

    override fun findById(iEntity: Long): ProductEntity? {
        return null
    }

    override fun findAll(): Boolean {
        val tempProduct = mutableListOf<ProductEntity>()
        val selectAll = """
            SELECT products.quantityRemaining,products.id,products.discount as discount_product,quantity,itbis,sellingPrice,purchaseprice,
            id_category,products.dateAdded,id_employee,expirationDate,products.code as code_product,products.description as description_product,
            Categories.discount as discount_category,Categories.code as code_categpry,Categories.description as description_category,userName
             from ${SqlCreateTables.products} INNER JOIN Employee  on 
            products.id_employee = Employee.id
            INNER JOIN Categories on 
            products.id_category = Categories.id
        """.trimIndent()
        Database.connect().use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(selectAll).use { resultSet ->
                    while (resultSet.next()){
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
                            resultSet.getString("description_product"),
                            resultSet.getDouble("sellingPrice"),
                            resultSet.getDouble("purchaseprice"),
                            resultSet.getDouble("discount_product"),
                            resultSet.getDouble("itbis"),
                            resultSet.getInt("quantity"),
                            resultSet.getInt("quantityRemaining"),
                            Util.timeStampToCalendar( resultSet.getTimestamp( "dateAdded") ),
                            Util.timeStampToCalendar( resultSet.getTimestamp( "expirationDate") ),
                            category,
                            employee)
                        tempProduct.add( prod )
                    }
                    products.addAll( tempProduct)
                }
            }
        }



        return false
    }

    override fun update(entity: ProductEntity): Boolean {
        return false
    }

    fun findProductByCode(code:String):ProductEntity?{

        val selectByCode = """
            SELECT  products.id,products.quantityRemaining,products.discount as discount_product,quantity,itbis,sellingPrice,purchaseprice,
            id_category,products.dateAdded,id_employee,expirationDate,products.code as code_product,products.description as description_product,
            Categories.discount as discount_category,Categories.code as code_categpry,Categories.description as description_category
             from products 
            INNER JOIN Categories on 
            products.id_category = Categories.id
            where code_product = ?
        """.trimIndent()


        Database.connect().use { connection ->
            connection.prepareStatement(selectByCode).use { preparedStatement ->
                preparedStatement.setString(1,code.padStart(12,'0'))
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
                    resultSet.getString("description_product"),
                    resultSet.getDouble("sellingPrice"),
                    resultSet.getDouble("purchaseprice"),
                    resultSet.getDouble("discount_product"),
                    resultSet.getDouble("itbis"),
                    resultSet.getInt("quantity"),
                    resultSet.getInt("quantityRemaining"),
                    Util.timeStampToCalendar( resultSet.getTimestamp( "dateAdded") ),
                    Util.timeStampToCalendar( resultSet.getTimestamp( "expirationDate") ),
                    category,
                    employee)
                prod.id = resultSet.getLong("id")

                if (prod.code == null || prod.code.isBlank()){
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
        Database.connect().use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(selectAll).use { resultSet ->
                    while (resultSet.next()){
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
                            resultSet.getString("description_product"),
                            resultSet.getDouble("sellingPrice"),
                            resultSet.getDouble("purchaseprice"),
                            resultSet.getDouble("discount_product"),
                            resultSet.getDouble("itbis"),
                            resultSet.getInt("quantity"),
                            resultSet.getInt("quantityRemaining"),
                            Util.timeStampToCalendar( resultSet.getTimestamp( "dateAdded") ),
                            Util.timeStampToCalendar( resultSet.getTimestamp( "expirationDate") ),
                            category,
                            employee)
                        tempProduct.add( prod )
                    }
                    return tempProduct
                }
            }
        }




    }

    }



















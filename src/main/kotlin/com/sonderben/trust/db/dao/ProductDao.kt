package com.sonderben.trust.db.dao


import com.sonderben.trust.Context
import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.SELECT_ALL_PRODUCT
import com.sonderben.trust.db.SqlDml.UPDATE_PRODUCT
import com.sonderben.trust.toTimestamp
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import java.sql.Connection
import java.sql.Statement
import java.sql.Timestamp
import java.util.Calendar

class ProductDao : CrudDao<ProductEntity> {


    init {
        findAll()
    }

    override fun save(entity: ProductEntity, connection: Connection): ProductEntity? {


        var insertProduct = """
            INSERT INTO ${SqlDdl.products} 
            (discount, itbis, purchasePrice, quantity, sellingPrice,id_category, dateAdded, id_employee, expirationDate, code, description,quantityRemaining,sellby ) 
            values(?,?,?,?,?,?,?,?,?,?,?,?,?)
        """.trimIndent()

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
            preparedStatement.setString(13, entity.sellBy)
            val rowCount = preparedStatement.executeUpdate()
            val lastId = Database.getLastId( connection )
            entity.id = lastId



            if (rowCount > 0) {
                return entity
            }


        }
        return null

    }

    override fun delete(idEntity: Long, connection: Connection): Long? {

        val deleteProduct = "delete from ${SqlDdl.products} where id = ?"

        connection.prepareStatement(deleteProduct).use { preparedStatement ->
            preparedStatement.setLong(1, idEntity)
            val rowCount = preparedStatement.executeUpdate()
            if (rowCount > 0) {
                return idEntity
            }
        }

        return null
    }

    override fun findById(iEntity: Long, connection: Connection): ProductEntity? {
        return null
    }

    override fun findAll(): List<ProductEntity> {

        val products = mutableListOf<ProductEntity>()

        Database.connect().use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(SELECT_ALL_PRODUCT).use { resultSet ->
                    while (resultSet.next()) {
                        val employee = EmployeeEntity()
                        val tempId = resultSet.getLong("id_employee")
                        employee.apply {
                            id = if (tempId == 0L) 1L else tempId
                            userName = resultSet.getString("userName") ?: "root"
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
                        products.add(prod)

                    }

                }
            }
        }

        return products

    }

    override fun update(entity: ProductEntity, connection: Connection): ProductEntity? {
        if (entity.id == null)
            throw RuntimeException(" id product can not be null")

        connection.prepareStatement(UPDATE_PRODUCT).use { preparedStatement ->
            preparedStatement.setDouble(1, entity.discount)
            preparedStatement.setDouble(2, entity.itbis)
            preparedStatement.setDouble(3, entity.purchasePrice)
            preparedStatement.setFloat(4, entity.quantity)
            preparedStatement.setDouble(5, entity.sellingPrice)
            preparedStatement.setLong(6, entity.category.id)
            preparedStatement.setLong(7, Context.currentEmployee.value.id)
            preparedStatement.setTimestamp(8, entity.expirationDate.toTimestamp())
            preparedStatement.setString(9, entity.code)
            preparedStatement.setString(10, entity.description)
            preparedStatement.setFloat(11, entity.quantityRemaining)
            preparedStatement.setString(12, entity.sellBy)
            preparedStatement.setLong(13, entity.id)

            val rowCount = preparedStatement.executeUpdate()


            if (rowCount > 0 || rowCount == Statement.SUCCESS_NO_INFO) {
                return entity

            }
        }
        return null
    }


}


package com.sonderben.trust.db.dao

import Database
import com.sonderben.trust.db.SqlCreateTables
import entity.CategoryEntity
import javafx.collections.FXCollections
import java.sql.SQLException


object CategoryDao {
     val categories  = FXCollections.observableArrayList<CategoryEntity>()

    init {
        findAll()
    }

    fun save(category:CategoryEntity) {
        val sqlinsert = """
            insert into
             ${SqlCreateTables.categories}(code, description, discount) 
            values (?, ?, ?)
        """.trimIndent()

        try {
            val statement = Database.connect().prepareStatement(sqlinsert)
                .apply {
                setString(1,category.code)
                setString(2,category.description)
                setDouble(3,category.discount)

            }
            var res = statement.executeUpdate()
            if (res>0){

                Database.connect().createStatement().use {statement ->
                    val resultSet =  statement.executeQuery( "SELECT last_insert_rowid() as last_id" )
                    category.id=resultSet.getLong("last_id")

                    categories.add(category)

                }


            }
        }catch (e:Exception){
            throw e
        }finally {
            Database.connect().close()
        }
    }

    private fun findAll() {
        val tempCategories: MutableList<CategoryEntity> = ArrayList()
        try {

                val selectAll = "SELECT * FROM ${SqlCreateTables.categories}"
            Database.connect().prepareStatement(selectAll).use { preparedStatement ->
                    preparedStatement.executeQuery().use { resultSet ->
                        while (resultSet.next()) {

                            val category = CategoryEntity()
                            category.apply {
                                 id = resultSet.getLong("id")
                                 code = resultSet.getString("code")
                                 description = resultSet.getString("description")
                                 discount = resultSet.getDouble("discount")
                            }

                            tempCategories.add(category)
                            categories.addAll( tempCategories )
                        }
                    }
                }

        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    fun delete(idEntity:Long):Boolean{
        Database.connect().prepareStatement(
            "DELETE  FROM ${SqlCreateTables.categories} WHERE id = ?"
        ).use { statement ->
            statement.setLong(1,idEntity)
            val res = statement.executeUpdate()
            if (res>0){
                categories.removeIf {
                    it.id == idEntity
                }
            }
            return res>0
        }
        Database.connect().close()
    }


}
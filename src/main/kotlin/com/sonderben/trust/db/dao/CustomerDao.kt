package com.sonderben.trust.db.dao

import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.DELETE_CUSTOMER
import com.sonderben.trust.db.SqlDml.FIND_CUSTOMER_BY_CODE
import com.sonderben.trust.db.SqlDml.FIND_CUSTOMER_BY_ID
import com.sonderben.trust.db.SqlDml.INSERT_CUSTOMER
import com.sonderben.trust.db.SqlDml.SELECT_ALL_CUSTOMER
import com.sonderben.trust.db.SqlDml.SPENDING_OR_FREQUENT_CUSTOMER
import com.sonderben.trust.db.SqlDml.UPDATE_CUSTOMER
import entity.CustomerEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import java.sql.Connection
import java.sql.SQLException
import java.sql.Timestamp


class CustomerDao : CrudDao<CustomerEntity> {




    override fun save(entity: CustomerEntity, connection: Connection): CustomerEntity? {




                connection.prepareStatement(INSERT_CUSTOMER).use { preparedStatement ->
                    preparedStatement.setTimestamp(1,Timestamp(entity.birthDay.time.time))
                    val tempCode = entity.code.padStart(12,'0')
                    preparedStatement.setString(2,tempCode)
                    preparedStatement.setString(3,entity.direction)
                    preparedStatement.setLong(4,entity.point)
                    preparedStatement.setString(5,entity.email)
                    preparedStatement.setString(6,entity.firstName)
                    preparedStatement.setString(7,entity.genre)
                    preparedStatement.setString(8,entity.lastName)
                    preparedStatement.setString(9,entity.passport)
                    preparedStatement.setString(10,entity.telephone)


                    val rowCount = preparedStatement.executeUpdate()
                    entity.id = Database.getLastId( connection )

                    if (rowCount > 0){
                        return entity
                    }



                }


            return null

    }

    override fun delete(idEntity: Long, connection: Connection): Long? {


                connection.prepareStatement(DELETE_CUSTOMER).use { preparedStatement ->
                    preparedStatement.setLong(1,idEntity)

                    if (preparedStatement.executeUpdate()>0){
                        return idEntity
                    }
                }

            return null

    }



    override fun findById(iEntity: Long, connection: Connection): CustomerEntity? {


                connection.prepareStatement(FIND_CUSTOMER_BY_ID).use { preparedStatement ->
                    preparedStatement.setLong(1,iEntity)

                    preparedStatement.executeQuery().use {resultSet ->
                            if (resultSet.next()){
                                val customer = CustomerEntity()
                                customer.apply {
                                    this.id = iEntity
                                    firstName = resultSet.getString("firstName")
                                    passport = resultSet.getString("passport")
                                    lastName = resultSet.getString("lastName")
                                    genre = resultSet.getString("genre")
                                    direction = resultSet.getString("direction")
                                    email = resultSet.getString("email")
                                    telephone = resultSet.getString("telephone")
                                    birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                                    code = resultSet.getString("code")
                                    point = resultSet.getLong("point")

                                }
                                return customer
                            }else{
                                return null
                            }


                    }
                }



    }

    override fun findAll(): List<CustomerEntity> {
       val customers = mutableListOf<CustomerEntity>()

                Database.connect().use { connection ->
                    connection.createStatement().use {statement ->
                        statement.executeQuery( SELECT_ALL_CUSTOMER ).use { resultSet ->
                            while (resultSet.next()){
                                val id = resultSet.getLong("id")
                                val customer = CustomerEntity()
                                customer.apply {
                                    this.id = id
                                    firstName = resultSet.getString("firstName")
                                    passport = resultSet.getString("passport")
                                    lastName = resultSet.getString("lastName")
                                    genre = resultSet.getString("genre")
                                    direction = resultSet.getString("direction")
                                    email = resultSet.getString("email")
                                    telephone = resultSet.getString("telephone")
                                    birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                                    code = resultSet.getString("code")
                                    point = resultSet.getLong("point")

                                }

                                customers.add( customer )
                            }


                        }
                        println("findAll: "+customers)


                    }
                }

           return customers
    }



    override fun update(entity: CustomerEntity, connection: Connection): CustomerEntity? {

        connection.prepareStatement( UPDATE_CUSTOMER ).use { preparedStatement ->
                    preparedStatement.setTimestamp(1, Timestamp(entity.birthDay.time.time))


                    preparedStatement.setString(2, entity.direction)

                    preparedStatement.setString(3, entity.email)
                    preparedStatement.setString(4, entity.firstName)
                    preparedStatement.setString(5, entity.genre)
                    preparedStatement.setString(6, entity.lastName)
                    preparedStatement.setString(7, entity.passport)
                    preparedStatement.setString(8, entity.telephone)
                    preparedStatement.setLong(9, entity.id)


                    val rowCount = preparedStatement.executeUpdate()


                    if (rowCount>0){
                        return entity

                    }


                }
        return null


    }

    fun findByCode(code: String, connection: Connection):CustomerEntity?{

            connection.prepareStatement( FIND_CUSTOMER_BY_CODE ).use { preparedStatement ->
                preparedStatement.setString(1,code.padStart(12,'0'))
                preparedStatement.executeQuery().use { resultSet ->

                    val customer = CustomerEntity()
                    customer.apply {
                        this.id = resultSet.getLong("id")
                        firstName = resultSet.getString("firstName")
                        passport = resultSet.getString("passport")
                        lastName = resultSet.getString("lastName")
                        genre = resultSet.getString("genre")
                        direction = resultSet.getString("direction")
                        email = resultSet.getString("email")
                        telephone = resultSet.getString("telephone")
                        birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                        this.code = resultSet.getString("code")
                        point = resultSet.getLong("point")

                    }



                    if (customer.id != null){
                        return customer
                    }



                }

            }


        return null;
    }

    fun updatePoint(id:Long,point: Long,connection: Connection): Boolean {
        val updateEmployee = "update  ${SqlDdl.customers} set point = point + ? where id = ?"

        connection.prepareStatement(updateEmployee).use {preparedStatement ->

                preparedStatement.setLong(1,point)
                preparedStatement.setLong(2,id)

                val rowCount = preparedStatement.executeUpdate()

                return rowCount > 0


            }


    }

    fun spendingOrFrequentCustomer(orderBy:String,connection: Connection):List<SpendingOrFrequentCustomer>{
        val sfc = mutableListOf<SpendingOrFrequentCustomer>()


            connection.prepareStatement( SPENDING_OR_FREQUENT_CUSTOMER ).use { preparedStatement ->
                preparedStatement.setString(1,orderBy)
                preparedStatement.executeQuery().use { resultSet ->
                    while (resultSet.next()){
                        val temp = SpendingOrFrequentCustomer(
                            resultSet.getString("customerCode"),
                            resultSet.getLong("point"),
                            resultSet.getInt("totalProductBought"),
                            resultSet.getDouble("totalSpend"),
                            resultSet.getInt("frequency")
                        )
                        sfc.add( temp )
                    }
                    return sfc
                }
            }

    }

    data class SpendingOrFrequentCustomer(
        var cusomerCode:String,
        var point:Long,
        var totalProductBought:Int,
        var totalSpend:Double,
        var frequency:Int
    )

}

package com.sonderben.trust.db.dao

import Database.DATABASE_NAME
import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import entity.CustomerEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.sql.SQLException
import java.sql.Timestamp


object CustomerDao : CrudDao<CustomerEntity> {


   var customers = FXCollections.observableArrayList<CustomerEntity>()
    init {
        findAll()
    }
    override fun save(entity: CustomerEntity): Completable {
        return Completable.create {emetter->
            val insertEmployee = buildString {
                append("Insert into ${SqlCreateTables.customers} ")
                append("( birthDay,code,direction,point,email,firstName,genre,lastName,passport,telephone ) ")
                append("values (?,?,?,?,?,?,?,?,?,?) ")
            }
            Database.connect(DATABASE_NAME).use { connection ->

                connection.prepareStatement(insertEmployee).use {preparedStatement ->
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
                    entity.id = Database.getLastId()

                    if (rowCount<=0){
                        connection.rollback()
                        emetter.onError(Throwable("can not save customer: $entity"))

                    }


                    customers.add( entity )


                    emetter.onComplete()


                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())

    }

    override fun delete(idEntity: Long): Completable {
        return  Completable.create {emitter ->
            Database.connect("").use { connection ->
                connection.prepareStatement("DELETE FROM ${SqlCreateTables.customers} WHERE id = ?").use { preparedStatement ->
                    preparedStatement.setLong(1,idEntity)
                    customers.removeIf { it.id==idEntity }
                    if (preparedStatement.executeUpdate()>0){
                        emitter.onComplete()
                    }
                }

            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }



    override fun findById(iEntity: Long): Maybe<CustomerEntity> {
        return Maybe.create { emitter->
            val selectAll = "SELECT * FROM ${SqlCreateTables.customers} WHERE id = ?"
            Database.connect(DATABASE_NAME).use {connection ->
                connection.prepareStatement(selectAll).use {statement ->
                    statement.setLong(1,iEntity)
                    statement.executeQuery().use {resultSet ->
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

                            if( customer.id == null) emitter.onComplete()else emitter.onSuccess(customer)
                    }
                }
            }
        }

    }

    override fun findAll(): Boolean {
        val selectAll = "SELECT * FROM ${SqlCreateTables.customers}"
        val tempEmployees = mutableListOf<CustomerEntity>()
        Database.connect(DATABASE_NAME).use {connection ->
            connection.createStatement().use {statement ->
                statement.executeQuery(selectAll).use {resultSet ->
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

                        tempEmployees.add( customer )
                    }
                    customers.addAll( tempEmployees )
                    tempEmployees.clear();
                }
                println("findAll: "+customers)
                return true

            }

        }
    }



    override fun update(entity: CustomerEntity): Completable {
        return Completable.create {emitter->
            if (entity.id==null)
                throw SQLException("id entity can not be null")

            if (findById(entity.id)==null)
                throw SQLException("id entity don't exist")
            val updateCustomer = """
            update  ${SqlCreateTables.customers}
             set birthDay =   ?,
             direction = ? ,
             email = ? ,
             firstName = ? ,
             genre = ? ,
             lastName = ? ,
             passport = ? ,
             telephone = ? 
             where id = ?
        """.trimIndent()


            Database.connect(DATABASE_NAME).use { connection ->


                connection.prepareStatement(updateCustomer).use { preparedStatement ->
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
                        customers[ customers.indexOf( entity ) ] = entity
                        emitter.onComplete()
                    }else{
                        emitter.onError(Throwable("can not update the customer, id: $entity "))
                    }






                }


            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())

    }

    fun findByCode(code: String):CustomerEntity?{
        Database.connect(DATABASE_NAME).use {connection ->
            connection.prepareStatement("select * from ${SqlCreateTables.customers} where code = ?").use { preparedStatement ->
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

        }
        return null;
    }
    fun updatePoint(id:Long,point: Long): Boolean {
        val updateEmployee = "update  ${SqlCreateTables.customers} set point = point + ? where id = ?"

        Database.connect(DATABASE_NAME).use { connection ->

            connection.prepareStatement(updateEmployee).use {preparedStatement ->

                preparedStatement.setLong(1,point)
                preparedStatement.setLong(2,id)

                val rowCount = preparedStatement.executeUpdate()

                return rowCount > 0


            }
        }

    }

    fun spendingOrFrequentCustomer(orderBy:String="totalSpend"):List<SpendingOrFrequentCustomer>{
        val sfc = mutableListOf<SpendingOrFrequentCustomer>()
        val select = """
            SELECT  c.code as customerCode,c.point,sum(ps.quantity) as totalProductBought,sum(ps.total) as totalSpend ,count(iv.id) as frequency
            from customers as c
            INNER JOIN invoices as iv on c.id = iv.id_customer
            INNER join invoiceProductSealed as ips on ips.id_invoice = iv.id
            INNER join productSealed as ps on ps.id = ips.id_product_sealed
            GROUP BY customerCode
            ORDER by ? DESC limit 100
        """.trimIndent()
        Database.connect("").use { connection ->
            connection.prepareStatement(select).use { preparedStatement ->
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
    }

    data class SpendingOrFrequentCustomer(
        var cusomerCode:String,
        var point:Long,
        var totalProductBought:Int,
        var totalSpend:Double,
        var frequency:Int
    )

}

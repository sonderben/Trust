package com.sonderben.trust.db.dao


import Database
import Database.DATABASE_NAME
import com.sonderben.trust.Util
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.INSERT_EMPLOYEE
import com.sonderben.trust.db.SqlDml.INSERT_ENTERPRISE
import com.sonderben.trust.db.SqlDml.INSERT_ROLE
import com.sonderben.trust.db.SqlDml.INSERT_SCHEDULE
import com.sonderben.trust.db.SqlDml.SELECT_ENTERPRISE
import com.sonderben.trust.db.SqlDml.UPDATE_EMPLOYEE
import com.sonderben.trust.db.SqlDml.UPDATE_ENTERPRISE
import com.sonderben.trust.db.SqlDml.UPDATE_SCHEDULER
import com.sonderben.trust.model.Role
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.EnterpriseEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.sql.Timestamp

object EnterpriseDao:CrudDao<EnterpriseEntity> {
    val enterprises: ObservableList<EnterpriseEntity> = FXCollections.observableArrayList()

    init {
       try {
           findAll()
       }catch (_:Exception){}
    }
    override fun save(entity: EnterpriseEntity): Completable {
        Database.connect(DATABASE_NAME).autoCommit = false

        return Completable.create { emitter->
            val employee = entity.employee
            if (employee.role == null ){
                throw Exception("Role can not be null")
            }


            var lastIdEmployeeAdded = 0L
            Database.connect(DATABASE_NAME).use { connection ->
                connection.autoCommit = false
                connection.prepareStatement(INSERT_EMPLOYEE).use { preparedStatement ->
                    preparedStatement.setString(1,employee.bankAccount)
                    preparedStatement.setString(2,employee.direction)
                    preparedStatement.setString(3,employee.email)
                    preparedStatement.setString(4,employee.firstName)
                    preparedStatement.setString(5,employee.genre)
                    preparedStatement.setString(6,employee.lastName)
                    preparedStatement.setString(7,employee.passport)
                    preparedStatement.setString(8,employee.password)
                    preparedStatement.setString(9,employee.telephone)
                    preparedStatement.setString(10,employee.userName)
                    preparedStatement.setTimestamp(11, Timestamp(employee.birthDay.time.time))
                    preparedStatement.setLong(12,saveRole(employee.role) )

                    val rowCount = preparedStatement.executeUpdate()


                    if (rowCount>0){
                        lastIdEmployeeAdded = Database.getLastId()
                        entity.id = lastIdEmployeeAdded

                        for (schedule in employee.schedules){
                            connection.prepareStatement(INSERT_SCHEDULE).use { ps ->
                                ps.setInt(1,schedule.workDay)
                                ps.setFloat(2,schedule.startHour)
                                ps.setFloat(3,schedule.endHour)
                                ps.setLong(4,lastIdEmployeeAdded)
                                val rowCount2 = ps.executeUpdate()
                                if (rowCount2<0){
                                    connection.rollback()
                                    connection.autoCommit = true
                                    emitter.onError( Throwable("unable to add  table: (${SqlDdl.schedules})") )

                                }else{
                                    schedule.id = Database.getLastId()
                                }
                            }
                        }
                    }else{
                        connection.rollback()
                        connection.autoCommit = true
                        emitter.onError( Throwable("cannot add employee") )

                    }




                    connection.prepareStatement(INSERT_ENTERPRISE).use { ps2->
                        ps2.setString(1,entity.name)
                        ps2.setString(2,entity.direction)
                        ps2.setString(3,entity.telephone)
                        ps2.setTimestamp(4,Timestamp(entity.foundation.time.time))
                        ps2.setString(5,entity.website)
                        ps2.setString(6,entity.categoryString)
                        ps2.setString(7,entity.invoiceTemplate)
                        ps2.setString(8,entity.invoiceTemplateHtml)
                        ps2.setLong(9,lastIdEmployeeAdded)
                        if (ps2.executeUpdate()>0){
                            connection.commit()
                            connection.autoCommit = true
                            emitter.onComplete()
                            findAll()
                        }else{
                            connection.rollback()
                            connection.autoCommit = false
                            emitter.onError( Throwable( "unable to add  table: (${SqlDdl.schedules})" ) )
                        }
                    }


                }

            }

            CategoryDao.save( CategoryEntity("0000","General",0.0) )
                .subscribe()
            RoleDao.getIntence().save( Role( "Seller", mutableListOf(ScreenEnum.SALE) ) )
                .subscribe()
        }
    }

    override fun delete(idEntity: Long): Completable {
       return Completable.create {

       }.subscribeOn(Schedulers.io())
           .observeOn(JavaFxScheduler.platform())
    }

    override fun findById(iEntity: Long): Maybe<EnterpriseEntity> {
        return Maybe.create {  }
    }

    private fun saveRole(role:Role):Long{


        val con = Database.connect(DATABASE_NAME)
        val ps = con.prepareStatement(INSERT_ROLE)
        ps.setString(1,role.name)
        ps.setString(2,role.screens.joinToString(",") { it.name })
        val rowCount = ps.executeUpdate()
        if (rowCount<=0) {
            con.rollback()
            con.autoCommit = true
            throw Exception(" can not add role")
        }
        val roleId = Database.getLastId()
        role.id = roleId


        return roleId
    }

    override fun findAll() {
        Single.create { emitter ->
            if (enterprises.isNotEmpty())
                enterprises.clear()
            Database.connect(DATABASE_NAME).use { connection->
                connection.createStatement().use { statement ->

                    statement.executeQuery( SELECT_ENTERPRISE ).use { resultSet ->
                        while (resultSet.next()){
                            val role = Role()
                            role.id = resultSet.getLong("ri")
                            role.name = resultSet.getString("rm")

                            val emp = EmployeeEntity(
                                resultSet.getString("empf"),
                                resultSet.getString("empp"),
                                resultSet.getString("empl"),
                                resultSet.getString("empg"),
                                resultSet.getString("empd"),
                                resultSet.getString("empe"),
                                resultSet.getString("emptel"),
                                Util.timeStampToCalendar( resultSet.getTimestamp("empb")),
                                resultSet.getString("empbank"),
                                resultSet.getString("empu"),
                                resultSet.getString("emppwd"),
                                role,
                                listOf()
                            )
                            emp.id = resultSet.getLong("empid")
                            enterprises.add(
                                EnterpriseEntity(
                                    resultSet.getLong("enid"),
                                    resultSet.getString("ne"),
                                    resultSet.getString("ed"),
                                    resultSet.getString("et"),
                                    Util.timeStampToCalendar( resultSet.getTimestamp("ef") ),
                                    resultSet.getString("ew"),
                                    resultSet.getString("ec"),
                                    emp,
                                    resultSet.getString("ei"),
                                    resultSet.getString("ein"),
                                )
                            )
                            emitter.onSuccess(enterprises)
                        }
                    }

                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )
            .subscribe ()
    }

    override fun update(entity: EnterpriseEntity): Completable {
        Database.connect(DATABASE_NAME).autoCommit = false

        return Completable.create { emitter->

            var lastIdEmployeeAdded = 0L
            Database.connect(DATABASE_NAME).use { connection ->
                connection.autoCommit = false
                connection.prepareStatement(UPDATE_EMPLOYEE).use { preparedStatement ->
                    preparedStatement.setString(1,entity.employee.bankAccount)
                    preparedStatement.setString(2,entity.employee.direction)
                    preparedStatement.setString(3,entity.employee.email)
                    preparedStatement.setString(4,entity.employee.firstName)
                    preparedStatement.setString(5,entity.employee.genre)
                    preparedStatement.setString(6,entity.employee.lastName)
                    preparedStatement.setString(7,entity.employee.passport)
                    preparedStatement.setString(8,entity.employee.password)
                    preparedStatement.setString(9,entity.employee.telephone)
                    preparedStatement.setString(10,entity.employee.userName)
                    preparedStatement.setTimestamp(11,Timestamp(entity.employee.birthDay.time.time))
                    preparedStatement.setLong(12,entity.employee.role.id)
                    preparedStatement.setLong(13,entity.employee.id)



                    if (preparedStatement.executeUpdate()>0){
                        lastIdEmployeeAdded = entity.employee.id


                        for (schedule in entity.employee.schedules){
                            connection.prepareStatement(UPDATE_SCHEDULER).use { ps ->
                                preparedStatement.setInt(1, schedule.workDay)
                                preparedStatement.setFloat(2, schedule.startHour)
                                preparedStatement.setFloat(3, schedule.endHour)
                                preparedStatement.setLong(4, schedule.idEmployee)
                                preparedStatement.setLong(5, schedule.id)
                                val rowCount2 = ps.executeUpdate()
                                if (rowCount2<0){
                                    connection.rollback()
                                    connection.autoCommit = true
                                    emitter.onError( Throwable("unable to update  table: (${SqlDdl.schedules})") )

                                }
                            }
                        }
                    }else{
                        connection.rollback()
                        connection.autoCommit = true
                        emitter.onError( Throwable("cannot update employee") )

                    }




                    connection.prepareStatement(UPDATE_ENTERPRISE).use { ps2->
                        ps2.setString(1,entity.name)
                        ps2.setString(2,entity.direction)
                        ps2.setString(3,entity.telephone)
                        ps2.setTimestamp(4,Timestamp(entity.foundation.time.time))
                        ps2.setString(5,entity.website)
                        ps2.setString(6,entity.categoryString)
                        ps2.setString(7,entity.invoiceTemplate)
                        ps2.setString(8,entity.invoiceTemplateHtml)
                        ps2.setLong(9,lastIdEmployeeAdded)
                        ps2.setLong(10,entity.id)
                        if (ps2.executeUpdate()>0){
                            connection.commit()
                            connection.autoCommit = true
                            emitter.onComplete()
                            findAll()
                        }else{
                            connection.rollback()
                            connection.autoCommit = false
                            emitter.onError( Throwable( "unable to update  table: (${SqlDdl.schedules})" ) )
                        }
                    }


                }
            }
        }
    }
}
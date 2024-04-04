package com.sonderben.trust.db.dao

import Database
import Database.DATABASE_NAME
import com.sonderben.trust.Context
import com.sonderben.trust.Util
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.DELETE_EMPLOYEE
import com.sonderben.trust.db.SqlDml.EMPLOYEE_LOGIN
import com.sonderben.trust.db.SqlDml.FIND_EMPLOYEE_BY_ID
import com.sonderben.trust.db.SqlDml.INSERT_EMPLOYEE
import com.sonderben.trust.db.SqlDml.INSERT_SCHEDULE
import com.sonderben.trust.db.SqlDml.SELECT_ALL_EMPLOYEE_INCLUDE_ADMIN_EXCLUDE_MAIN_ADMIN
import com.sonderben.trust.db.SqlDml.SELECT_ALL_EMPLOYEE_EXCLUDE_ALL_ADMINS
import com.sonderben.trust.db.SqlDml.UPDATE_EMPLOYEE
import com.sonderben.trust.model.Role
import entity.EmployeeEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import java.sql.Timestamp


class EmployeeDao private constructor(): CrudDao<EmployeeEntity> {


    companion object{
        @Volatile private var  instance:EmployeeDao? = null
        fun getInstance():EmployeeDao{
            if (instance==null){
                synchronized(this){
                    if (instance == null){
                        instance = EmployeeDao()
                    }
                }
            }
            return instance!!
        }

        fun clearInstance() {
            instance = null
        }
    }


   var employees = FXCollections.observableArrayList<EmployeeEntity>()
    init {

        //findAll()

    }
    override fun save(entity: EmployeeEntity): Completable {
       return Completable.create { emitter->
           if (entity.role == null || entity.role.id == null){
               throw Exception("Role or Role.id can not be null")
           }

           var lastIdEmployeeAdded: Long
           Database.connect(DATABASE_NAME).use { connection ->
               connection.autoCommit = false

               connection.prepareStatement(INSERT_EMPLOYEE).use { preparedStatement ->
                   preparedStatement.setString(1,entity.bankAccount)
                   preparedStatement.setString(2,entity.direction)
                   preparedStatement.setString(3,entity.email)
                   preparedStatement.setString(4,entity.firstName)
                   preparedStatement.setString(5,entity.genre)
                   preparedStatement.setString(6,entity.lastName)
                   preparedStatement.setString(7,entity.passport)
                   preparedStatement.setString(8,entity.password)
                   preparedStatement.setString(9,entity.telephone)
                   preparedStatement.setString(10,entity.userName)
                   preparedStatement.setTimestamp(11,Timestamp(entity.birthDay.time.time))
                   preparedStatement.setLong(12,entity.role.id)

                   val rowCount = preparedStatement.executeUpdate()


                   if (rowCount>0){
                       lastIdEmployeeAdded = Database.getLastId()
                       entity.id = lastIdEmployeeAdded

                       for (schedule in entity.schedules){
                           connection.prepareStatement(INSERT_SCHEDULE).use {ps ->
                               ps.setInt(1,schedule.workDay)
                               ps.setFloat(2,schedule.startHour)
                               ps.setFloat(3,schedule.endHour)
                               ps.setLong(4,lastIdEmployeeAdded)
                               val rowCount2 = ps.executeUpdate()
                               if (rowCount2<0){
                                   connection.rollback()
                                   emitter.onError( Throwable("Can not save schedule: $schedule") )

                               }else{
                                   schedule.id = Database.getLastId()
                               }
                           }
                       }

                   }else{
                       connection.rollback()
                       emitter.onError( Throwable("Can not save employee: $entity") )
                   }
                   employees.add( entity )
                   connection.commit()
                   emitter.onComplete()


               }
           }
       }.subscribeOn(Schedulers.io())
           .observeOn(JavaFxScheduler.platform())

    }

    override fun delete(idEntity: Long): Completable {

        return Completable.create { emitter->


            Database.connect(DATABASE_NAME).prepareStatement(DELETE_EMPLOYEE).use { ps ->
                ps.setLong(1,idEntity)
                val rowCount = ps.executeUpdate()
                if (rowCount>0){
                    employees.removeIf{
                        it.id == idEntity
                    }
                    emitter.onComplete()
                }else{
                    println("can not delete employee")
                    Database.connect(DATABASE_NAME).rollback()
                    emitter.onError( Throwable("can not delete employee") )
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )



    }

    override fun findById(iEntity: Long): Maybe<EmployeeEntity> {
        return Maybe.create { emitter->


            Database.connect(DATABASE_NAME).use {connection ->
                connection.prepareStatement(FIND_EMPLOYEE_BY_ID).use {preparedStatement ->
                    preparedStatement.setLong(1,iEntity)

                    preparedStatement.executeQuery().use { resultSet ->

                        val employee = EmployeeEntity()
                        employee.apply {
                            id = resultSet.getLong("id")
                            firstName = resultSet.getString("firstName")
                            passport = resultSet.getString("passport")
                            lastName = resultSet.getString("lastName")
                            genre = resultSet.getString("genre")
                            direction = resultSet.getString("direction")
                            email = resultSet.getString("email")
                            telephone = resultSet.getString("telephone")
                            birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                            bankAccount = resultSet.getString("bankAccount")
                            this.userName = resultSet.getString("userName")
                            this.password = resultSet.getString("password")

                            role = Database.findRolesByIdEmployee( resultSet.getLong("id_role") )
                            schedules =  Database.findScheduleByIdEmployee( id )
                        }
                        if (employee.id == null) emitter.onComplete() else emitter.onSuccess(employee)

                    }




                }

            }
        }

    }

    override fun findAll() {

        employees.clear()


        Single.create< List<EmployeeEntity> > { emitter ->


            Database.connect(DATABASE_NAME).use {connection ->
                connection.createStatement().use {statement ->


                    val TEMP_SELECT_ALL_EMPLOYEE:String = if (Context.currentEmployee.get().isAdmin){
                        SELECT_ALL_EMPLOYEE_INCLUDE_ADMIN_EXCLUDE_MAIN_ADMIN
                    }else{
                        SELECT_ALL_EMPLOYEE_EXCLUDE_ALL_ADMINS
                    }
                    statement.executeQuery( TEMP_SELECT_ALL_EMPLOYEE ).use { resultSet ->
                        while (resultSet.next()){

                            val employee = EmployeeEntity()
                            val tempRole = Role()

                            employee.apply {
                                id = resultSet.getLong("id")
                                firstName = resultSet.getString("firstName")
                                passport = resultSet.getString("passport")
                                lastName = resultSet.getString("lastName")
                                genre = resultSet.getString("genre")
                                direction = resultSet.getString("direction")
                                email = resultSet.getString("email")
                                telephone = resultSet.getString("telephone")
                                birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                                bankAccount = resultSet.getString("bankAccount")
                                this.userName = resultSet.getString("userName")
                                this.password = resultSet.getString("password")

                                role = tempRole.apply {

                                    id = resultSet.getLong("roleId")

                                    screens = resultSet.getString("screens").split(",".toRegex()).map { value ->ScreenEnum.valueOf(value) }
                                        .toMutableList() /*Arrays.stream(

                                        resultSet.getString("screens").split(",".toRegex())
                                            .toTypedArray())
                                        .map { value: String? -> ScreenEnum.valueOf(value!!.trim()) }
                                        .toList()*/
                                    name = resultSet.getString("name")
                                }
                                //Database.findRolesByIdEmployee( resultSet.getLong("id_role") )
                                schedules =  Database.findScheduleByIdEmployee( id )
                            }

                            employees.add( employee )
                        }

                        emitter.onSuccess( employees )

                    }


                }

            }

        }
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )
            .subscribe ()

    }

    fun login(userName:String,password:String):Maybe<EmployeeEntity>{
        return Maybe.create<EmployeeEntity> { emitter ->
            Database.connect(DATABASE_NAME).use {connection ->
                connection.prepareStatement( EMPLOYEE_LOGIN ).use { preparedStatement ->
                    preparedStatement.setString(1,userName)
                    preparedStatement.setString(2,password)
                    preparedStatement.executeQuery().use { resultSet ->

                        val employee = EmployeeEntity()
                        val id = resultSet.getLong("id")
                        if ( id!=0L){
                            employee.apply {
                                this.id = id
                                firstName = resultSet.getString("firstName")
                                passport = resultSet.getString("passport")
                                lastName = resultSet.getString("lastName")
                                genre = resultSet.getString("genre")
                                direction = resultSet.getString("direction")
                                email = resultSet.getString("email")
                                telephone = resultSet.getString("telephone")
                                birthDay = Util.timeStampToCalendar( resultSet.getTimestamp("birthDay") )
                                bankAccount = resultSet.getString("bankAccount")
                                this.userName = resultSet.getString("userName")
                                this.password = resultSet.getString("password")

                                role = Database.findRolesByIdEmployee( resultSet.getLong("id_role") )
                                schedules =  Database.findScheduleByIdEmployee( id )
                            }
                            emitter.onSuccess(employee)
                        }
                        System.err.println("user dont found: "+resultSet.fetchSize)
                        emitter.onComplete()




                    }
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    override fun update(entity: EmployeeEntity): Completable {
        return Completable.create {emitter->


            Database.connect("").use { connection ->
                connection.prepareStatement( UPDATE_EMPLOYEE ).use {preparedStatement ->
                    preparedStatement.setString(1,entity.bankAccount)
                    preparedStatement.setString(2,entity.direction)
                    preparedStatement.setString(3,entity.email)
                    preparedStatement.setString(4,entity.firstName)
                    preparedStatement.setString(5,entity.genre)
                    preparedStatement.setString(6,entity.lastName)
                    preparedStatement.setString(7,entity.passport)
                    preparedStatement.setString(8,entity.password)
                    preparedStatement.setString(9,entity.telephone)
                    preparedStatement.setString(10,entity.userName)
                    preparedStatement.setTimestamp(11,Timestamp(entity.birthDay.time.time))
                    preparedStatement.setLong(12,entity.role.id)
                    preparedStatement.setLong(13,entity.id)

                    val rowCount = preparedStatement.executeUpdate()
                    if (rowCount>0){

                        employees[ employees.indexOf( entity ) ] = entity
                        emitter.onComplete()

                    }else{
                        emitter.onError( Throwable("Can not update employee: $entity") )
                    }



                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    fun updateCredential(userName: String,password: String):Completable{
        val updateCredential = "UPDATE ${SqlDdl.employees} SET userName = ?,password = ? WHERE id = ?"
        return Completable.create { emitter->
            Database.connect("").use { connection ->
                connection.prepareStatement(updateCredential).use { preparedStatement ->
                    preparedStatement.setString(1,userName)
                    preparedStatement.setString(2,password)
                    preparedStatement.setLong(3,Context.currentEmployee.get().id)

                    if (preparedStatement.executeUpdate()>0){
                        emitter.onComplete()
                    }else{
                        emitter.onError( Throwable("can't update credential") )
                    }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }
}


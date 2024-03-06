package com.sonderben.trust.db.dao

import Database
import Database.DATABASE_NAME
import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlCreateTables
import entity.EmployeeEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import java.sql.Timestamp


object EmployeeDao : CrudDao<EmployeeEntity> {


   var employees = FXCollections.observableArrayList<EmployeeEntity>()
    init {

        findAll()

    }
    override fun save(entity: EmployeeEntity): Completable {
       return Completable.create { emitter->
           if (entity.role == null || entity.role.id == null){
               throw Exception("Role or Role.id can not be null")
           }
           val insertEmployee = buildString {
               append("Insert into ${SqlCreateTables.employees} ")//passport
               append("(bankAccount,direction,email,firstName,genre,lastName,passport,password,telephone,userName,birthDay,id_role) ")
               append("values (?,?,?,?,?,?,?,?,?,?,?,?) ")
           }


           val insertIntoSchedule = buildString {
               append("INSERT INTO ${SqlCreateTables.schedules }")
               append(" (workDay,start_hour,end_hour,id_employee) ")
               append(" values( ?,?,?,?);")
           }
           var lastIdEmployeeAdded = 0L
           Database.connect(DATABASE_NAME).use { connection ->
               connection.autoCommit = false
               connection.prepareStatement(insertEmployee).use {preparedStatement ->
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
                           connection.prepareStatement(insertIntoSchedule).use {ps ->
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

            val deleteEmployee = "delete from ${SqlCreateTables.employees} where id = ?"
            Database.connect(DATABASE_NAME).prepareStatement(deleteEmployee).use { ps ->
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
            val selectAll = "SELECT * FROM ${SqlCreateTables.employees} where id = ?"

            Database.connect(DATABASE_NAME).use {connection ->
                connection.prepareStatement(selectAll).use {statement ->
                    statement.setLong(1,iEntity)

                    statement.executeQuery(selectAll).use {resultSet ->

                        val id = resultSet.getLong("id")
                        val employee = EmployeeEntity()

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

                        if (employee.id == null) emitter.onComplete() else emitter.onSuccess(employee)


                    }


                }

            }
        }

    }

    override fun findAll(): Boolean {
        var isSuccess = false
         val selectAll = "SELECT * FROM ${SqlCreateTables.employees}"
        val tempEmployees = mutableListOf<EmployeeEntity>()

        Single.create< List<EmployeeEntity> > { emitter ->


            Database.connect(DATABASE_NAME).use {connection ->
                connection.createStatement().use {statement ->
                    statement.executeQuery(selectAll).use {resultSet ->
                        while (resultSet.next()){
                            val id = resultSet.getLong("id")
                            val employee = EmployeeEntity()

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

                            tempEmployees.add( employee )
                        }
                        isSuccess = true
                        emitter.onSuccess( tempEmployees )

                        //employees.addAll( tempEmployees )
                        //tempEmployees.clear();
                    }


                }

            }

        }
            .subscribeOn(Schedulers.io())
            .observeOn( JavaFxScheduler.platform() )
            .subscribe { data ->
                employees.addAll( data )
            }
        return isSuccess
    }

    fun login(userName:String,password:String):Maybe<EmployeeEntity>{
        return Maybe.create<EmployeeEntity> { emitter ->
            Database.connect(DATABASE_NAME).use {connection ->
                connection.prepareStatement("select * from ${SqlCreateTables.employees} where userName= ? and password =? ;").use { preparedStatement ->
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
        return Completable.create {  }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }
}


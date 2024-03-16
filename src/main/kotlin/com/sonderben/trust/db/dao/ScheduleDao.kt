package com.sonderben.trust.db.dao

import Database
import Database.DATABASE_NAME
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.UPDATE_SCHEDULER
import entity.ScheduleEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler


object ScheduleDao : CrudDao<ScheduleEntity> {

    init {

        findAll()

    }

    override fun save(entity: ScheduleEntity): Completable {
        return Completable.create { emitter ->


            val insertIntoSchedule = buildString {
                append("INSERT INTO ${SqlDdl.schedules}")
                append(" (workDay,start_hour,end_hour,id_employee) ")
                append(" values( ?,?,?,?);")
            }
            Database.connect(DATABASE_NAME).use { connection ->


                connection.prepareStatement(insertIntoSchedule).use { ps ->
                    ps.setInt(1, entity.workDay)
                    ps.setFloat(2, entity.startHour)
                    ps.setFloat(3, entity.endHour)
                    ps.setLong(4, entity.idEmployee)

                    if (ps.executeUpdate() <= 0) {
                        emitter.onError(Throwable("Can not save schedule: $entity"))

                    } else {
                        emitter.onComplete()
                    }
                }

            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())

    }

    override fun delete(idEntity: Long): Completable {

        return Completable.create { emitter ->

            val deleteEmployee = "delete from ${SqlDdl.schedules} where id = ?"
            Database.connect(DATABASE_NAME).prepareStatement(deleteEmployee).use { ps ->
                ps.setLong(1, idEntity)

                if (ps.executeUpdate() > 0) {
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable("can not delete employee"))
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())


    }

    override fun findById(iEntity: Long): Maybe<ScheduleEntity> {
        return Maybe.create {

        }

    }

    override fun findAll() {


    }

    override fun update(entity: ScheduleEntity): Completable {
        return Completable.create { emitter ->

            Database.connect("").use { connection ->
                connection.prepareStatement( UPDATE_SCHEDULER ).use { preparedStatement ->
                    preparedStatement.setInt(1, entity.workDay)
                    preparedStatement.setFloat(2, entity.startHour)
                    preparedStatement.setFloat(3, entity.endHour)
                    preparedStatement.setLong(4, entity.idEmployee)
                    preparedStatement.setLong(5, entity.id)

                    val rowCount = preparedStatement.executeUpdate()
                    if (rowCount > 0) {
                        emitter.onComplete()

                    } else {
                        emitter.onError(Throwable("Can not update employee: $entity"))
                    }


                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }
}


package com.sonderben.trust.db.dao

import Database
import com.sonderben.trust.db.SqlDdl
import com.sonderben.trust.db.SqlDml.UPDATE_SCHEDULER
import entity.ScheduleEntity
import java.sql.Connection


class ScheduleDao : CrudDao<ScheduleEntity> {


    override fun save(entity: ScheduleEntity, connection: Connection): ScheduleEntity? {


        val insertIntoSchedule = buildString {
            append("INSERT INTO ${SqlDdl.schedules}")
            append(" (workDay,start_hour,end_hour,id_employee) ")
            append(" values( ?,?,?,?);")
        }



        connection.prepareStatement(insertIntoSchedule).use { ps ->
            ps.setInt(1, entity.workDay)
            ps.setString(2, entity.startHour)
            ps.setString(3, entity.endHour)
            ps.setLong(4, entity.idEmployee)

            if (ps.executeUpdate() > 0) {
                entity.id = Database.getLastId( connection )
                return entity
            }
        }

        return null


    }

    override fun delete(idEntity: Long, connection: Connection): Long? {


        val deleteEmployee = "delete from ${SqlDdl.schedules} where id = ?"
        connection.prepareStatement(deleteEmployee).use { ps ->
            ps.setLong(1, idEntity)

            if (ps.executeUpdate() > 0) {
                return idEntity
            }
        }

        return null

    }

    override fun findById(iEntity: Long, connection: Connection): ScheduleEntity? {
        return null

    }

    override fun findAll(): List<ScheduleEntity> {
        return emptyList()

    }

    override fun update(entity: ScheduleEntity, connection: Connection): ScheduleEntity? {

        connection.prepareStatement(UPDATE_SCHEDULER).use { preparedStatement ->
            preparedStatement.setInt(1, entity.workDay)
            preparedStatement.setString(2, entity.startHour)
            preparedStatement.setString(3, entity.endHour)
            preparedStatement.setLong(4, entity.idEmployee)
            preparedStatement.setLong(5, entity.id)

            val rowCount = preparedStatement.executeUpdate()
            if (rowCount > 0) {
                return entity

            }


        }
        return null

    }
}


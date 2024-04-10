package com.sonderben.trust.db.service

import com.sonderben.trust.db.dao.ScheduleDao
import entity.ScheduleEntity


class ScheduleService private constructor(dao: ScheduleDao): CrudService<ScheduleDao,ScheduleEntity>(dao) {

    companion object{
        @Volatile private var instance:ScheduleService? = null
        fun getInstance(): ScheduleService {
            if (instance==null){
                synchronized(this){
                    if (instance==null){
                        instance = ScheduleService( ScheduleDao() )
                    }
                }
            }
            return instance!!
        }
        fun clear(): Unit {
            instance = null
        }
    }
}


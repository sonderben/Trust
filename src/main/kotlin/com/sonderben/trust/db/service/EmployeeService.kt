package com.sonderben.trust.db.service

import Database
import com.sonderben.trust.db.dao.EmployeeDao
import entity.EmployeeEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler


class EmployeeService private constructor(dao: EmployeeDao): CrudService<EmployeeDao,EmployeeEntity>(dao) {




    companion object{
        @Volatile private var  instance:EmployeeService? = null
        fun getInstance():EmployeeService{
            if (instance==null){
                synchronized(this){
                    if (instance == null){
                        instance = EmployeeService(EmployeeDao())

                    }
                }
            }
            return instance!!
        }

        fun clearInstance() {
            instance = null
        }
    }






    fun updateCredential(userName: String,password: String):Completable{

        return Completable.create { emitter->
            //Database.connect("").use { connection ->
                val isSave = dao.updateCredential(userName, password)
                if (isSave){
                    emitter.onComplete()
                }else{
                    emitter.onError( Throwable("can not update credential.") )
                }
            //}
        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }
}


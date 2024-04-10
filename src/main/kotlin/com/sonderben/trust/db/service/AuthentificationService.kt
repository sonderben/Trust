package com.sonderben.trust.db.service

import com.sonderben.trust.db.dao.AuthentificationDao
import entity.EmployeeEntity
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler

class AuthentificationService private constructor(val dao:AuthentificationDao){
    companion object{
        @Volatile private var instance:AuthentificationService?=null
        fun getInstance(): AuthentificationService {
            if (instance==null){
                synchronized(this){
                    if (instance==null){
                        instance = AuthentificationService( AuthentificationDao() )
                    }
                }
            }
            return instance!!
        }
    }

    fun login(userName:String,password:String):Maybe<EmployeeEntity>{
        return Maybe.create { emitter->
            val emp = dao.login( userName, password )
            if ( emp == null ){
                emitter.onComplete()
            }
            else{
                emitter.onSuccess(emp)
            }
        }.subscribeOn( Schedulers.io() )
            .observeOn( JavaFxScheduler.platform() )
    }
}

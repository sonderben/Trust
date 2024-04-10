package com.sonderben.trust.db.service

import com.sonderben.trust.db.dao.AdministratorDao
import entity.AdminEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler


class AdministratorService private constructor(dao: AdministratorDao): CrudService<AdministratorDao,AdminEntity>(dao) {




    companion object{
        @Volatile private var  instance:AdministratorService? = null
        fun getInstance():AdministratorService{
            if (instance==null){
                synchronized(this){
                    if (instance == null){
                        instance = AdministratorService(AdministratorDao())

                    }
                }
            }
            return instance!!
        }

        fun clearInstance() {
            instance = null
        }
    }






    fun updateCredential(password: String):Completable{

        return Completable.create { emitter->

                val isSave = dao.updateCredential(password)
                if (isSave){
                    emitter.onComplete()
                }else{
                    emitter.onError( Throwable("can not update credential.") )
                }

        }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }
}


package com.sonderben.trust.db.service


import com.sonderben.trust.db.dao.CustomerDao
import entity.CustomerEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler


class CustomerService private constructor(dao: CustomerDao) : CrudService<CustomerDao, CustomerEntity>(dao) {

    companion object{
        @Volatile private var instance: CustomerService?=null
        public fun getInstance(): CustomerService {
            if (instance == null){
                synchronized(this){
                    if(instance ==null)
                        instance = CustomerService(CustomerDao())
                }
            }
            return instance!!
        }
    }


    fun findByCode(code: String): Maybe<CustomerEntity> {
        return Maybe.create{emitter->
            Database.connect().use { connection ->

                val c = dao.findByCode( code, connection )
                if (c!=null){
                    emitter.onSuccess(c)
                }else{
                    emitter.onComplete()
                }

            }
        }

    }

    fun updatePoint(id: Long, point: Long): Completable {


        return Completable.create { emitter->
            Database.connect().use { connection ->

                val isUpdate = dao.updatePoint(id, point, connection)
                if ( isUpdate ){
                    emitter.onComplete()
                }else{
                    emitter.onError( Throwable("can not update point of this customer") )
                }

            }
        }.subscribeOn( Schedulers.io() )
            .observeOn( JavaFxScheduler.platform() )

    }

    fun spendingOrFrequentCustomer(orderBy:String="totalSpend"):Single< List<CustomerDao.SpendingOrFrequentCustomer> >  {
        return Single.create{emitter->

           Database.connect().use { connection->
               val entities = dao.spendingOrFrequentCustomer(orderBy, connection)
               emitter.onSuccess(entities)
           }

        }
    }



}

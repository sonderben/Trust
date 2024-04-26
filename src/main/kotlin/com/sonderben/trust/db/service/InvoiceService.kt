package com.sonderben.trust.db.service

import Database
import com.sonderben.trust.db.dao.InvoiceDao
import entity.InvoiceEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler

class InvoiceService private constructor(dao: InvoiceDao): CrudService<InvoiceDao, InvoiceEntity>(dao) {

    companion object{
        @Volatile private var instance:InvoiceService?=null
        fun getInstance(): InvoiceService {
            if(instance==null){
                synchronized(this){
                    if (instance==null){
                        instance = InvoiceService( InvoiceDao() )
                    }
                }
            }
            return instance!!
        }
    }

    fun productSealed(from:Long,to:Long): Single<List<InvoiceDao.ProductSealed>> {
        return Single.create { emitter->
            Database.connect().use { connection ->
                val ps = dao.productSealed(connection,from,to)

                emitter.onSuccess(ps)
            }
        }.subscribeOn( Schedulers.io() )
            .observeOn( JavaFxScheduler.platform() )

    }

    fun findByInvoiceCode( invoiceCode: String): Single<List<InvoiceDao.ProductToReturned>> {
        return Single.create{emitter->
            Database.connect().use { connection ->
                val i = dao.findByInvoiceCode(connection, invoiceCode)
                emitter.onSuccess(i)
            }
        }.subscribeOn( Schedulers.io() )
            .observeOn( JavaFxScheduler.platform() )

    }

    fun saveProductReturned( idInvoice: Long, idProductSealed: List<Long>,
        idEmployee: Long, reason: String, action: String): Completable {

        return Completable.create { emitter->
            Database.connect().use { connection->
                val isSave = dao.saveProductReturned(connection,idInvoice, idProductSealed, idEmployee, reason, action)

                if (isSave)
                    emitter.onComplete()
                else
                    emitter.onError( Throwable("can not save product returned") )
            }
        }.subscribeOn( Schedulers.io() )
            .observeOn( JavaFxScheduler.platform() )

    }

    fun getProductReturned(): Single<List<InvoiceDao.ProductReturned>> {

        return Single.create{emitter->
            Database.connect().use { connection ->
                val pr = dao.getProductReturned(connection)
                emitter.onSuccess(pr)
            }
        }.subscribeOn( Schedulers.io() )
            .observeOn( JavaFxScheduler.platform() )



    }

}



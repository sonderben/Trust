package com.sonderben.trust.db.service

import com.sonderben.trust.controller.ProductDetails
import entity.ProductEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

class ProductDetailService private constructor(val dao:ProductDetails){

    companion object{
        @Volatile private var instance:ProductDetailService?=null
        fun getInstance(): ProductDetailService {
            if (instance==null){
                synchronized(this){
                    if (instance==null){
                        instance = ProductDetailService( ProductDetails() )
                    }
                }
            }
            return instance!!
        }
        fun clearInstance(): Unit {
            instance=null
        }
    }

    fun findProductByCode(code: String): Maybe<ProductEntity> {


        return Maybe.create { emitter->
            Database.connect().use { connection ->
                val p = dao.findProductByCode(code, connection)
                if(p!=null){
                    emitter.onSuccess(p)
                }else{
                    emitter.onComplete()
                }
            }
        }
    }


    fun findProductsExpired(): Single<MutableList<ProductEntity>> {

        return Single.create { emitter->
            Database.connect().use { connection ->
                val p = dao.findProductsExpired( connection )

                emitter.onSuccess(p)

            }
        }

    }


    fun updateQuantityRemaining(codeProduct: String, qtyBought: Float): Completable {

        return Completable.create {emitter->
            Database.connect().use { connection->
                val rowCount = dao.updateQuantityRemaining(codeProduct, qtyBought, connection)
                if (rowCount>0){
                    emitter.onComplete()
                }else{
                    emitter.onError( Throwable(" can not update quantity remaining of this product") )
                }
            }
        }



    }

    fun bestSellers(qty: Float, frequency: Int, benefit: Float): Single<List<ProductDetails.BestSeller>> {

        return Single.create { emitter->
            Database.connect().use { connection ->
                val bs = dao.bestSellers(connection,qty,frequency,benefit)
                emitter.onSuccess(bs)
            }
        }

    }


}
package com.sonderben.trust.db.service


import com.sonderben.trust.db.dao.ProductDao
import entity.ProductEntity

class ProductService private constructor(dao: ProductDao): CrudService<ProductDao, ProductEntity>(dao) {

    companion object{
        @Volatile private var instance:ProductService? = null
        fun getInstance(): ProductService {
            if(instance==null){
                synchronized(this){
                    if (instance==null){
                        instance = ProductService( ProductDao() )
                    }
                }
            }
            return instance!!
        }
        fun clear() {
            instance = null
        }
    }
}


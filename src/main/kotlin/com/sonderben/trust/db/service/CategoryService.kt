
package com.sonderben.trust.db.service

import com.sonderben.trust.db.dao.CategoryDao
import entity.CategoryEntity


class CategoryService private constructor (dao: CategoryDao) : CrudService<CategoryDao, CategoryEntity>(dao){


    companion object{
        @Volatile private var instance:CategoryService? = null

        fun getInstance(): CategoryService {


                if ( instance ==null ){
                    synchronized(this){
                        if (instance==null)
                            instance = CategoryService( CategoryDao() )
                    }
                }

            return instance!!
        }
    }







}
package com.sonderben.trust.db.service



import com.sonderben.trust.db.dao.EnterpriseDao
import entity.EnterpriseEntity

class EnterpriseService private constructor (dao: EnterpriseDao): CrudService<EnterpriseDao, EnterpriseEntity>(dao) {


    companion object{
        @Volatile private var instance: EnterpriseService? = null
        public fun getInstance(): EnterpriseService {
            if (instance ==null){
                synchronized(this){
                    if (instance == null){
                        instance = EnterpriseService( EnterpriseDao() )
                    }
                }
            }
            return instance!!
        }
    }


}
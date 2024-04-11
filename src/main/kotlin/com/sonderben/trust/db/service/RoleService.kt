package com.sonderben.trust.db.service


import com.sonderben.trust.db.dao.RoleDao
import entity.Role

class RoleService private constructor(dao: RoleDao): CrudService<RoleDao, Role>(dao) {
    companion object{
        @Volatile private var instance: RoleService? = null
        fun getInstance(): RoleService {

            if (instance ==null){
                synchronized(this){
                    if (instance ==null){
                        instance = RoleService( RoleDao() )
                    }
                }
            }
            return instance!!
        }
        fun clearInstance(){
            instance = null
        }
    }


















}
package com.sonderben.trust.db.dao

import javafx.collections.FXCollections

interface CrudDao<E> {
     fun save(entity:E):Boolean
    fun delete(idEntity:Long):Boolean

    fun findById(iEntity:Long):E?
     fun findAll():Boolean

    fun update(entity:E):Boolean
}
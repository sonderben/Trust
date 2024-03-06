package com.sonderben.trust.db.dao

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javafx.collections.FXCollections

interface CrudDao<E> {
     fun save(entity:E):Completable
    fun delete(idEntity:Long):Completable

    fun findById(iEntity:Long):Maybe<E>
     fun findAll():Boolean

    fun update(entity:E):Completable
}
package com.sonderben.trust.db.dao

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import javafx.collections.FXCollections

interface CrudDao<E> {
     fun save(entity:E):Completable
    fun delete(idEntity:Long):Completable

    fun findById(iEntity:Long):Maybe<E>
     fun findAll()

    fun update(entity:E):Completable
}
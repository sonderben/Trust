package com.sonderben.trust.db.dao

import entity.BaseEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import javafx.collections.FXCollections
import java.sql.Connection

interface CrudDao<E : BaseEntity> {
    fun save(entity: E, connection: Connection): E?
    fun delete(idEntity: Long, connection: Connection): Long?

    fun findById(iEntity: Long, connection: Connection): E?
    fun findAll(): List<E>

    fun update(entity: E, connection: Connection): E?
}
package com.sonderben.trust.db.service

import Database
import com.sonderben.trust.db.dao.CrudDao
import entity.BaseEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections
import javafx.collections.ObservableList

abstract class CrudService<T : CrudDao<E>, E : BaseEntity>(var dao: T) {


    public val entities: ObservableList<E> = FXCollections.observableArrayList(dao.findAll())
    fun save(entity: E): Completable {
        return Completable.create { emitter ->
            Database.connect().use { connection ->
                val entitySave = dao.save(entity, connection)
                if (entitySave != null) {
                    entities.add(entitySave)
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable("can not save ${entity::class}"))
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    fun delete(idEntity: Long): Completable {
        return Completable.create { emitter ->
            Database.connect().use { connection ->
                val id = dao.delete(idEntity, connection)
                if (id != null) {
                    entities.removeIf { idEntity.equals(it.id) }
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable("can not delete entity with id ${idEntity}"))
                }

            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    fun findById(iEntity: Long): Maybe<E> {
        return Maybe.create { emitter ->
            Database.connect().use { connection ->
                val entity = dao.findById(iEntity, connection)
                if (entity != null) {
                    emitter.onSuccess(entity)
                } else {
                    emitter.onError(Throwable("can not find entity with id ${iEntity}"))

                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    fun findAll() {
        Single.create<List<E>> { emitter ->
            emitter.onSuccess(dao.findAll())
        }.subscribe({ listEntities ->
            entities.clear()
            entities.addAll(listEntities)
        }, {})

    }

    fun update(entity: E): Completable {
        return Completable.create { emitter ->
            Database.connect().use { connection ->
                val entityUpdate = dao.update(entity, connection)
                if (entityUpdate != null) {
                    entities[entities.indexOf(entity)] = entity
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable("can not update  ${entity::class}"))

                }

            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())

    }
}

/*private fun<E> rr(obj:Any,emitter: CompletableEmitter,stringClass: Class<Any>): Unit {
    if (obj != null){
        emitter.onComplete(  )
    }else{
        emitter.onError( Throwable("can not save ${stringClass}") )
    }
}*/

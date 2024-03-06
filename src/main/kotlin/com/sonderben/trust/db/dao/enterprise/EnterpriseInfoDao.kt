package com.sonderben.trust.db.dao.enterprise

import Database.TRUST_DB
import com.sonderben.trust.db.SqlCreateTables
import com.sonderben.trust.db.dao.CrudDao
import entity.enterprise.EnterpriseInfo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.collections.FXCollections

object EnterpriseInfoDao:CrudDao<EnterpriseInfo> {
    val enterprisesInfos = FXCollections.observableArrayList<EnterpriseInfo>()

    init {
        findAll()
    }
    override fun save(entity: EnterpriseInfo): Completable {
        return Completable.create { emitter->
            val insert = """
            Insert into ${SqlCreateTables.createTrustEnterpriseInfo}
            (path,name) values (?,?)
        """.trimIndent()

            Database.connect(TRUST_DB).use { connection ->
                connection.prepareStatement(insert).use { preparedStatement ->
                    preparedStatement.setString(1,entity.path)
                    preparedStatement.setString(2,entity.name)
                    val isSave = preparedStatement.executeUpdate()>0
                    if (isSave) emitter.onComplete() else emitter.onError(Throwable("Can not save EnterpriseInfo: $entity"))
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    override fun delete(idEntity: Long): Completable {
        return Completable.create { emitter->

        }.subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }

    override fun findById(iEntity: Long): Maybe<EnterpriseInfo> {
       return Maybe.create {  }
    }

    override fun findAll(): Boolean {
        val selectAll = "Select * from ${SqlCreateTables.trustEnterpriseInfo};"
        Database.connect(TRUST_DB).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(selectAll).use { resultSet ->
                    val temp = mutableListOf<EnterpriseInfo>()
                    while (resultSet.next()){
                        temp.add(
                            EnterpriseInfo(
                                resultSet.getString("path"),
                                resultSet.getString("name"))
                        )
                    }
                    enterprisesInfos.addAll(temp)
                    return true
                }
            }
        }
    }

    override fun update(entity: EnterpriseInfo): Completable {
        return Completable.create {  }
            .subscribeOn(Schedulers.io())
            .observeOn(JavaFxScheduler.platform())
    }
}
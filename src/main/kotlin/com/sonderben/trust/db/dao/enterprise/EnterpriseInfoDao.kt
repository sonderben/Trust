package com.sonderben.trust.db.dao.enterprise

import Database.TRUST_DB
import com.sonderben.trust.db.SqlCreateTables
import com.sonderben.trust.db.dao.CrudDao
import entity.enterprise.EnterpriseInfo
import javafx.collections.FXCollections

object EnterpriseInfoDao:CrudDao<EnterpriseInfo> {
    val enterprisesInfos = FXCollections.observableArrayList<EnterpriseInfo>()

    init {
        findAll()
    }
    override fun save(entity: EnterpriseInfo): Boolean {
        val insert = """
            Insert into ${SqlCreateTables.createTrustEnterpriseInfo}
            (path,name) values (?,?)
        """.trimIndent()

        Database.connect(TRUST_DB).use { connection ->
            connection.prepareStatement(insert).use { preparedStatement ->
                preparedStatement.setString(1,entity.path)
                preparedStatement.setString(2,entity.name)
                return preparedStatement.executeUpdate()>0
            }
        }
    }

    override fun delete(idEntity: Long): Boolean {
        return false
    }

    override fun findById(iEntity: Long): EnterpriseInfo? {
       return null
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

    override fun update(entity: EnterpriseInfo): Boolean {
       return false
    }
}
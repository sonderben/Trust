package com.sonderben.trust.db.dao

import com.sonderben.trust.db.SqlCreateTables
import entity.EmployeeEntity


object EmployeeDao : CrudDao<EmployeeEntity> {
    /*
     birthDay timestamp,
        bankAccount, direction varchar(255),email varchar(255),firstName varchar(255),
        genre varchar(255),
        lastName varchar(255),
        passport varchar(255),
        password varchar(255),
        telephone varchar(255),
        userName varchar(255) unique not null,
        id_role integer not null,
        FOREIGN KEY(id_role)
            REFERENCES $roles (id)
     */
    override fun save(employee: EmployeeEntity): Boolean {
        val insertEmployee = buildString {
            append("Insert into ${SqlCreateTables.employees} ")
            append("(bankAccount,direction,email,firstName,genre,lastName,passport,password,telephone,userName) ")
            append("values (?,?,?,?,?,?,?,?,?,?) ")
        }
        val insertRoleEmployee = buildString {
            append("insert into ${SqlCreateTables.employeesRoles} ")
            append("(id_role,id_employee) ")
            append("values (?,?)")
        }
        var lastIdEmployeeAdded = 0L
        Database.connect().use { connection ->
            connection.autoCommit = false
            connection.prepareStatement(insertEmployee).use {preparedStatement ->
                preparedStatement.setString(1,employee.bankAccount)
                preparedStatement.setString(2,employee.direction)
                preparedStatement.setString(3,employee.email)
                preparedStatement.setString(4,employee.firstName)
                preparedStatement.setString(5,employee.genre)
                preparedStatement.setString(6,employee.lastName)
                preparedStatement.setString(7,employee.passport)
                preparedStatement.setString(8,employee.password)
                preparedStatement.setString(9,employee.telephone)
                preparedStatement.setString(10,employee.userName)

                val rowCount = preparedStatement.executeUpdate()


                if (rowCount>0){
                    lastIdEmployeeAdded = Database.getLastId()
                    for (role in employee.roleList){
                        connection.prepareStatement(insertRoleEmployee).use {ps ->
                            ps.setLong(1,role.id)
                            ps.setLong(2,lastIdEmployeeAdded)
                             val rowCount2 = ps.executeUpdate()
                            if (rowCount2<0){
                                throw Exception(" unable to add relationship table (${SqlCreateTables.employeesRoles})")
                            }
                        }
                    }
                }
                connection.commit()

                return rowCount>0


            }
        }

    }

    override fun delete(idEntity: Long): Boolean {
        return false
    }

    override fun findById(iEntity: Long): EmployeeEntity? {
        return null
    }

    override fun findAll(): Boolean {
        return false
    }

    override fun update(entity: EmployeeEntity): Boolean {
        return false
    }
}
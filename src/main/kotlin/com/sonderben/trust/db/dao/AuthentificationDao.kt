package com.sonderben.trust.db.dao

import com.sonderben.trust.Util
import com.sonderben.trust.db.SqlDml
import entity.EmployeeEntity

class AuthentificationDao {
    fun login(userName: String, password: String): EmployeeEntity? {

        Database.connect().use { connection ->
            if ( userName.equals("root",true) ){
                val admDao = AdministratorDao()
                val adms = admDao.findAll()

                if ( adms.isNotEmpty() && adms[0].password.equals( password )){
                    return adms[ 0 ]
                }
            }else{
                connection.prepareStatement(SqlDml.EMPLOYEE_LOGIN).use { preparedStatement ->
                    preparedStatement.setString(1, userName)
                    preparedStatement.setString(2, password)
                    preparedStatement.executeQuery().use { resultSet ->


                        // val id = resultSet.getLong("id")
                        if (resultSet.next()) {
                            val employee = EmployeeEntity()
                            employee.apply {
                                this.id = resultSet.getLong("id")
                                firstName = resultSet.getString("firstName")
                                passport = resultSet.getString("passport")
                                lastName = resultSet.getString("lastName")
                                genre = resultSet.getString("genre")
                                direction = resultSet.getString("direction")
                                email = resultSet.getString("email")
                                telephone = resultSet.getString("telephone")
                                birthDay = Util.timeStampToCalendar(resultSet.getTimestamp("birthDay"))
                                bankAccount = resultSet.getString("bankAccount")
                                this.userName = resultSet.getString("userName")
                                this.password = resultSet.getString("password")

                                role = Database.findRolesByIdEmployee(resultSet.getLong("id_role"))
                                schedules = Database.findScheduleByIdEmployee(id)
                            }
                            return employee
                        }


                    }
                }
            }

        }
        return null

    }
}
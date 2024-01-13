package com.sonderben.trust.model

import java.util.Calendar

data class Person(
    var firstName:String,var lastName:String,
    var birthDay:Calendar,var id:String,
    var genre:String,
    var direction:String,var email:String,
    var bankAccount:String,var telephone:String,
    var userName:String,var password:String,
    var roles:List<Role>,
    var schedule: List<Schedule>) {
}
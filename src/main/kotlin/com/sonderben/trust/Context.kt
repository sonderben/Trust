package com.sonderben.trust

import entity.EmployeeEntity
import javafx.beans.property.SimpleObjectProperty

object Context {
    var  currentEmployee:SimpleObjectProperty<EmployeeEntity> = SimpleObjectProperty()


    fun leerJson(){

    }
}
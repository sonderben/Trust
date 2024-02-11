package com.sonderben.trust

import javafx.scene.layout.VBox
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

fun VBox.hide(){
    if (this.isVisible){
        this.isVisible = false
        this.managedProperty().set(false)
    }else{
        this.isVisible = true
        this.managedProperty().set(true)
    }
}

fun Calendar.format(style:Int=DateFormat.MEDIUM):String{
    val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.getDefault())

    return dateFormat.format(this.time)

}
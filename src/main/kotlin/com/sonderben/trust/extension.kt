package com.sonderben.trust

import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.layout.VBox
import java.sql.Timestamp
import java.text.DateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.function.UnaryOperator

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

fun LocalDate.toCalendar():Calendar{
    return GregorianCalendar.from(this.atStartOfDay(ZoneId.systemDefault()))
}

fun Calendar.toLocalDate():LocalDate{
       return LocalDate.of(this.get(Calendar.YEAR), this.get(Calendar.MONTH) + 1, this.get(Calendar.DAY_OF_MONTH))
}

fun TextField.textTrim():String{
    return this.text.trim()
}

fun Timestamp.toCalendar():Calendar{

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this.time
        return calendar
}

fun TextField.onlyInt(){
    this.textFormatter = Factory.createFilterTextField()
}
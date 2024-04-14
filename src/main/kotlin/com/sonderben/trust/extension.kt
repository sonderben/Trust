package com.sonderben.trust

import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import org.json.simple.JSONObject
import java.sql.Timestamp
import java.text.DateFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun Node.changeVisibility(){
    if (this.isVisible){
        this.isVisible = false
        this.isManaged = false
    }else{
        this.isVisible = true
        this.isManaged = true
    }
}

fun Calendar.format(style:Int=DateFormat.MEDIUM):String{
    val dateFormat = DateFormat.getDateInstance(style,Locale.getDefault())

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

fun Calendar.toTimestamp():Timestamp{
    return Timestamp(this.timeInMillis)
}

fun TextField.onlyInt(){
    this.textFormatter = Factory.createFilterTextField()
}

 fun Double.toCurrency(): String {
    //val local = Locale("en","us");
    val format = NumberFormat.getCurrencyInstance()

    return format.format(this)
}

fun String.equalAtLeastOne(vararg strings: String,ignoreCase:Boolean=true):Boolean{
    for (str in strings){
        if (this.equals(str,ignoreCase))
            return true
    }
    return false
}


fun JSONObject.getBoolean(key:String) = this[key] as Boolean
fun JSONObject.getString(key:String) = this[key] as String
fun JSONObject.getDouble(key:String) = this[key] as Double
fun JSONObject.getJsonObject(key:String) = this[key] as JSONObject

package com.sonderben.trust

import javafx.event.Event
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
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
    this.textFormatter = Factory.intFilterTextField()
}
fun TextField.onlyFloat(){
    this.textFormatter = Factory.doubleFilterTextField()
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

fun center():Point2D {
    val  x = HelloApplication.primary.x + (HelloApplication.primary.width/2)
    val  y = HelloApplication.primary.y + (HelloApplication.primary.height/2)
    println( Point2D(x,y ))
    return Point2D(x,y)
}


fun JSONObject.getBoolean(key:String) = this[key] as Boolean
fun JSONObject.getString(key:String) = this[key] as String
fun JSONObject.getDouble(key:String) = this[key] as Double
fun JSONObject.getJsonObject(key:String) = this[key] as JSONObject

fun String.isValidEmail():Boolean{
    return this.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$".toRegex())
}

fun Float.isInt():Boolean{
    val s = this.toString().split(".")
    return ( s.size==1 || s[1].toInt()==0 )
}

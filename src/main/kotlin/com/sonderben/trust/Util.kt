package com.sonderben.trust

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object Util {
    val format = SimpleDateFormat("dd/m/yyyy"/*, Locale.getDefault()*/)

    fun formatDate( date:Date ):String{
        return format.format( date )
    }

    fun timeStampToCalendar(timestamp:Timestamp?):Calendar{
        if (timestamp!=null){
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp.time
            return calendar
        }

            val cal = Calendar.getInstance()
            cal.set(1111,11,11)
        return cal


    }
}
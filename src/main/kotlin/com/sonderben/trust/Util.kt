package com.sonderben.trust

import java.text.SimpleDateFormat
import java.util.*

object Util {
    val format = SimpleDateFormat("dd/m/yyyy"/*, Locale.getDefault()*/)

    fun formatDate( date:Date ):String{
        return format.format( date )
    }
}
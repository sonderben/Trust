package com.sonderben.trust

import entity.EmployeeEntity
import javafx.beans.property.SimpleObjectProperty
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.lang.RuntimeException
import java.text.DateFormat
import java.util.Date
import java.util.Locale

object Context {
    val path:String = "src/main/kotlin/com/sonderben/trust/config/preference.json"

    var  currentEmployee:SimpleObjectProperty<EmployeeEntity> = SimpleObjectProperty()
    var language:String=""
    fun start() {
        writeJson("language","jp")
        //readJson()
    }

    fun readJson():JSONObject{
        val parser = JSONParser()
        val json = parser.parse( FileReader( path ) )
        val jsonObj:JSONObject = json as JSONObject
        language = jsonObj["language"] as String
        //println( jsonObj.toJSONString() )
        when(language){
            "fr"->Locale.setDefault( Locale.FRENCH )
            "ht"->{
                val locale = Locale.Builder()
                    .setLanguage("ht")
                    .setRegion("HT")
                    .build()
                val date = Date()
                val datef = DateFormat.getDateInstance(DateFormat.FULL,locale)
                println( datef.format( date ) )

            }
        }
        return jsonObj
    }

    fun writeJson(key:Any,value:Any){
        val jsonObj = readJson()

        if (!jsonObj.containsKey(key))
            throw RuntimeException("can't update json preference file, key dont exist")

        jsonObj[key] = value
        println(jsonObj.toJSONString())
        try {

            FileWriter(path).use { fileW->
                fileW.write(jsonObj.toString())
                fileW.flush()
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}
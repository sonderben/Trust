package com.sonderben.trust

import com.sonderben.trust.db.service.EnterpriseService
import entity.EmployeeEntity
import entity.EnterpriseEntity
import javafx.beans.property.SimpleObjectProperty
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Paths
import java.util.*

object Context {
    val path: String = "data/preference.json"


    private val screenJson = readJson()["screen"] as JSONObject
    var screen:Screen = Screen(
        screenJson.getBoolean("isAlwaysOnTop"),
        screenJson.getBoolean("isFullScreen") ,
        screenJson.getDouble("width"),
        screenJson.getDouble("height"),
        screenJson.getDouble("x"),
        screenJson.getDouble("y")
    )


    var currentEmployee: SimpleObjectProperty<EmployeeEntity> = SimpleObjectProperty()
    private var enterpriseEntity:EnterpriseEntity? = null
    var language: String = ""
    init {
        val file = File(path)

        /*if(!file.exists()){
            val vv = file.createNewFile()
            println("isCreated: $vv")
            FileWriter(file).use {
                val a = """
                    {"existTables":false,"last_count_customer":0,"isAlwaysOnTop":true,"screen":{"isAlwaysOnTop":true,"width":720.0,"x":480.0,"y":125.0,"isFullScreen":false,"height":498.0},"language":"es"}
                """.trimIndent()
                it.write(a)
            }
        }*/
        if (EnterpriseService.getInstance().entities.isNotEmpty()){
            enterpriseEntity = EnterpriseService.getInstance().entities[0]
        }
    }
    fun start() {
        //writeJson("language", "jp")
        //readJson()
    }

    fun setLanguage() {

        language = readJson()["language"] as String

        when (language) {
            "fr" -> Locale.setDefault(Locale.FRENCH)
            "es" -> Locale.setDefault( Locale("es",
                "ES") )
            "en" -> Locale.setDefault( Locale.US )
            else -> Locale.setDefault( Locale.getDefault() )
        }

        println("default language is: ${Locale.getDefault()}")

    }

    fun readJson(): JSONObject {
        val parser = JSONParser()
        val json = parser.parse(FileReader(path))
        return json as JSONObject
    }

    fun writeJson(key: String, value: Any) {
        val jsonObj = readJson()

        if (!jsonObj.containsKey(key))
            throw RuntimeException("can't update json preference file, key dont exist")

        jsonObj[key] = value
        println(jsonObj.toJSONString())
        try {

            FileWriter(path).use { fileW ->
                fileW.write(jsonObj.toString())
                fileW.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setScreen( key: String, value: Any ){
        val jsonObj:JSONObject = readJson()
        val jsonScreenObj = readJson().getJsonObject("screen")

        if (!jsonScreenObj.containsKey(key))
            throw RuntimeException("can't update json preference file, key dont exist")

        jsonScreenObj[key] = value

        jsonObj["screen"] = jsonScreenObj
        try {

            FileWriter(path).use { fileW ->
                fileW.write(jsonObj.toString())
                fileW.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun defaultInvoice() = """
        <html dir="ltr">
<head></head>
<body contenteditable="true"><p style="text-align: center;"><span style="font-family: &quot;&quot;; font-weight: bold;">Enterprise name</span>
</p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">Category</span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">Direction</span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">Phone: telephone</span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">..............................................................................................................................................</span>
</p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">Cashier: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span><span
        style="font-family: &quot;&quot;;">&nbsp; Employee</span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">..............................................................................................................................................</span>
</p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;; font-weight: bold;">Desc. &nbsp; &nbsp; &nbsp; &nbsp;|Price &nbsp; &nbsp; |Qty &nbsp; &nbsp; &nbsp; |taxe &nbsp; &nbsp; |Total&nbsp;</span>
</p>
<p style="text-align: left;"><span style="font-family: &quot;&quot;; font-size: small;">Pan dulce &nbsp;</span><span
        style="font-family: &quot;&quot;; font-size: small;">&nbsp; &nbsp; 50.00 &nbsp; &nbsp; &nbsp; &nbsp;12.00 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 12.00 &nbsp; 44345.56</span>
</p>
<p style="text-align: left;"><span style="font-family: &quot;&quot;;">Total &nbsp;| &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 12.00 | &nbsp;40.00 &nbsp; 34343.90</span>
</p>
<p style="text-align: left;"><span style="font-family: &quot;&quot;; font-weight: bold;"><br></span></p>
<p style="text-align: left;"><span style="font-family: &quot;&quot;; font-weight: bold;">Discount &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;2345436.767</span>
</p>
<p style="text-align: left;"><span style="font-family: &quot;&quot;; font-weight: bold;">Total a pagar &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;45436355</span>
</p>
<p style="text-align: left;"><span style="font-family: &quot;&quot;;"><span style="font-weight: bold;">Cash &nbsp;43843534</span>&nbsp;</span>
</p>
<p style="text-align: left;"><span style="font-family: &quot;&quot;;"><br></span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">THANK YOU!</span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">Glad to see you again</span></p>
</body>
</html>
    """.trimIndent()

    data class Screen(val isAlwaysOnTop:Boolean,val isFullScreen:Boolean,val width:Double,val height:Double,val x:Double,val y:Double)
}









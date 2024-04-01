package com.sonderben.trust

import com.sonderben.trust.db.dao.EnterpriseDao
import entity.EmployeeEntity
import entity.EnterpriseEntity
import javafx.beans.property.SimpleObjectProperty
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.lang.RuntimeException
import java.text.DateFormat
import java.util.*

object Context {
    val path: String = "src/main/kotlin/com/sonderben/trust/config/preference.json"
    val resource = ResourceBundle.getBundle("com.sonderben.trust.i18n.string")

    var currentEmployee: SimpleObjectProperty<EmployeeEntity> = SimpleObjectProperty()
    private var enterpriseEntity:EnterpriseEntity? = null
    var language: String = ""
    init {
        if (EnterpriseDao.enterprises.isNotEmpty()){
            enterpriseEntity = EnterpriseDao.enterprises[0]
        }
    }
    fun start() {
        writeJson("language", "jp")
        //readJson()
    }

    fun setLanguage() {

        language = readJson()["language"] as String

        when (language) {
            "fr" -> Locale.setDefault(Locale.FRENCH)
            "ht" -> {
                val locale = Locale.Builder()
                    .setLanguage("ht")
                    .setRegion("HT")
                    .build()
                val date = Date()
                val datef = DateFormat.getDateInstance(DateFormat.FULL, locale)
                println(datef.format(date))

            }
        }

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

    fun defaultInvoice() = """
        <html dir="ltr">
<head></head>
<body contenteditable="true"><p style="text-align: center;"><span style="font-family: &quot;&quot;; font-weight: bold;">${enterpriseEntity?.name}</span>
</p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">${enterpriseEntity?.category.toString()}</span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">${enterpriseEntity?.direction}</span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">Phone: ${enterpriseEntity?.telephone}</span></p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">..............................................................................................................................................</span>
</p>
<p style="text-align: center;"><span style="font-family: &quot;&quot;;">Cashier: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span><span
        style="font-family: &quot;&quot;;">&nbsp; ${currentEmployee.get().fullName}</span></p>
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
}
package com.sonderben.trust

import Database
import Factory
import com.sonderben.trust.db.dao.EnterpriseDao.save
import com.sonderben.trust.model.Role
import entity.EmployeeEntity
import entity.EnterpriseEntity
import entity.ScheduleEntity
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.util.*
import java.util.List

class HelloApplication : Application() {

    override fun start(stage: Stage) {
        Database.createTable()
        try {
            /*val role = Role()
            role.id = 1L
            val emp = EmployeeEntity(
                "Admin",
                "12345",
                "Admin",
                "Male",
                "Admin",
                "admin@gmail.com",
                "11111",
                Calendar.getInstance(),
                "1",
                "root",
                "1234",
                role,
                List.of<ScheduleEntity>(ScheduleEntity(null, 1, 11.30f, 1f))
            )


            save(
                EnterpriseEntity(
                    "Acra motors",
                    "lascirie #1",
                    "509 340 5643",
                    Calendar.getInstance(),
                    "www.baw.com",
                    "PHARMACY",
                    emp,
                    "1",
                    "1"
                )
            )*/

             Database.prepopulate()

        }catch (e:Exception){
           // throw e
           // println(e.message)
        }
        primary = stage
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
        val scene = Scene(fxmlLoader.load(), 720.0, 440.0)


        //ScenicView.show( scene )



        primary.icons.add(
            Factory.createImage("image/shield.png")
        )

        stage.title = "Trust"
        stage.scene = scene

        //primary.initStyle(StageStyle.UNIFIED)
        primary.minHeight = 440.0
        primary.minWidth = 720.0
        stage.isAlwaysOnTop = true
        stage.show()

    }

    companion object {
        lateinit var primary: Stage
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
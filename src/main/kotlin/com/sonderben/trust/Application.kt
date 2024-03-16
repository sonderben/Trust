package com.sonderben.trust

import Database
import Factory
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.print.Printer
import javafx.scene.Scene
import javafx.stage.Stage
import javax.script.ScriptEngineManager

class HelloApplication : Application() {

    override fun start(stage: Stage) {
       // Database.createTrustTables()
        Context.start()
        Database.createTable()

        Context.readJson()




        primary = stage
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
        val scene = Scene(fxmlLoader.load(), 720.0, 440.0)

        //println("printer: ${Printer.getDefaultPrinter()}" )

        primary.centerOnScreen()


        //ScenicView.show( scene )



        primary.icons.add(
            Factory.createImage("image/shield.png")
        )

        stage.title = "Trust"
        stage.scene = scene


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
    println("kooooooool")
}
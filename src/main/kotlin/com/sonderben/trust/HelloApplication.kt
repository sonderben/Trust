package com.sonderben.trust

import Database
import Factory
import com.sonderben.trust.constant.Constant
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import printer.thermal.PrinterOptions


class HelloApplication : Application() {
    override fun start(stage: Stage) {

        Database.createTable()
        Context.start()

        Context.setLanguage()

        primary = stage
        primary.minHeight = 440.0
        primary.minWidth = 720.0
        primary.centerOnScreen()
        //primary.isAlwaysOnTop = true

        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"),Constant.resource)
        val scene = Scene(fxmlLoader.load(), 720.0, 440.0)
        stage.title = "Trust"
        stage.scene = scene



        //ScenicView.show( scene )




        primary.icons.add(
            Factory.createImage("image/shield.png")
        )


        stage.show()


    }

    companion object {
        lateinit var primary: Stage
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
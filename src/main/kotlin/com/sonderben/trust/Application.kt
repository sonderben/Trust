package com.sonderben.trust

import Database
import Factory
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import printer.thermal.PrinterOptions
import java.util.ResourceBundle
import java.util.ResourceBundle.getBundle


class HelloApplication : Application() {
//https://github.com/ReactiveX/RxJava/wiki/Error-Handling
    override fun start(stage: Stage) {

        Database.createTable()
        Context.start()

        Context.setLanguage()



        primary = stage
        val resource:ResourceBundle = getBundle("com.sonderben.trust.i18n.string")
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"),resource)
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
        //stage.isAlwaysOnTop = true
        stage.show()



        val p = PrinterOptions()
        p.print()





    }

    companion object {
        lateinit var primary: Stage
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
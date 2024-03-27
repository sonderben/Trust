package com.sonderben.trust

import Database
import Factory
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

        val Init = byteArrayOf(27, 64)
        println( "test 1 2: ${String(Init)}" )


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
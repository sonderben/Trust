package com.sonderben.trust

import Database
import Factory
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.scenicview.ScenicView

class HelloApplication : Application() {

    override fun start(stage: Stage) {
        Database.createTable()
        try {
            Database.prepopulate()
        }catch (_:Exception){}
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
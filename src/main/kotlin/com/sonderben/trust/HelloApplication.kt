package com.sonderben.trust

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {

    override fun start(stage: Stage) {
        primary = stage;
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 920.0, 640.0)
        stage.title = "Trust"
        stage.scene = scene
        stage.show()

    }

    companion object {
        lateinit var primary: Stage
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
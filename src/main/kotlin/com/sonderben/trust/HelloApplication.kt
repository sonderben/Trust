package com.sonderben.trust

import Database
import Factory
import com.sonderben.trust.constant.Constant
import javafx.application.Application
import javafx.application.HostServices
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage


class HelloApplication : Application() {
    override fun start(stage: Stage) {

        Database.createTable()
        Context.start()


        Context.setLanguage()
        //hostServices.showDocument("www.bendersonphanor.com")

        services = hostServices

        primary = stage
        primary.minHeight = 440.0
        primary.minWidth = 720.0
        if (primary.x<0 || primary.y<=0)
            primary.centerOnScreen()

        primary.x = Context.screen.x
        primary.y = Context.screen.y

        primary.width = Context.screen.width
        primary.height = Context.screen.height

        primary.isAlwaysOnTop = Context.screen.isAlwaysOnTop
        primary.isMaximized = Context.screen.isFullScreen



        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"),Constant.resource)
        val scene = Scene(fxmlLoader.load(), Context.screen.width, Context.screen.height)
        stage.title = "Trust"
        stage.scene = scene


        //ScenicView.show( scene )

        primary.icons.add(
            Factory.createImage("image/shield.png")
        )
        primary.maximizedProperty().addListener { observableValue, oldNewValue, newValue ->
            Context.setScreen("isFullScreen",newValue)
        }
        primary.alwaysOnTopProperty().addListener{ observableValue, oldNewValue, newValue->
            Context.setScreen("isAlwaysOnTop",newValue)
        }
        primary.widthProperty().addListener { observableValue, oldNewValue, newValue ->
            Context.setScreen("width",newValue)
        }
        primary.heightProperty().addListener { observableValue, oldNewValue, newValue ->
            Context.setScreen("height",newValue)
        }
        primary.xProperty().addListener { observableValue, number, number2 ->
            Context.setScreen("x",number2)
        }

        primary.yProperty().addListener { observableValue, number, number2 ->
            Context.setScreen("y",number2)
        }


        stage.show()


    }

    companion object {
        lateinit var primary: Stage
        lateinit var services:HostServices
    }
}

/*fun main() {
    Application.launch(HelloApplication::class.java)
}*/
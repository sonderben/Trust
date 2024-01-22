package com.sonderben.trust

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent

class HelloController {
    @FXML
    private lateinit var login: Button

    @FXML
    fun onLoginButtonClick(event: ActionEvent?) {

        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/main_view.fxml"))
        //val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("sale.fxml"))

        val scene = Scene(fxmlLoader.load(), 920.0, 640.0)
        HelloApplication.primary.scene = scene
    }

    @FXML
    fun onCreateNewSystemMouseClicked(event: MouseEvent) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("sale.fxml"))
        val scene = Scene(fxmlLoader.load(), 920.0, 640.0)
        HelloApplication.primary.scene = scene
    }
}
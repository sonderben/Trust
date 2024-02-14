package com.sonderben.trust

import com.sonderben.trust.controller.ConfigurationController
import com.sonderben.trust.db.dao.EmployeeDao
import javafx.beans.property.SimpleObjectProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import java.net.URL
import java.util.*

class LoginController : Initializable{
    @FXML
    private lateinit var login: Button
    @FXML
    private lateinit var password: PasswordField

    @FXML
    private lateinit var userNameTextField: TextField
    @FXML
    private lateinit var infoLabel: Label
    @FXML
    private lateinit var enterpriseCB: ChoiceBox<Any>


    @FXML
    fun onLoginButtonClick(event: ActionEvent?) {
        val employeeEntity = EmployeeDao.login(userNameTextField.text.trim(),password.text.trim())
        if ( employeeEntity != null ){
            Context.currentEmployee = SimpleObjectProperty(employeeEntity)
            val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/main_view.fxml"))
            val scene = Scene(fxmlLoader.load(), 720.0, 440.0)
            HelloApplication.primary.scene = scene
        }else{
            infoLabel.isVisible = true
            infoLabel.text = "username or password is wrong"
        }


    }

    @FXML
    fun onCreateNewSystemMouseClicked() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/configuration.fxml"))
        val scene = Scene(fxmlLoader.load(), 920.0, 640.0)

        val configurationController = fxmlLoader.getController<ConfigurationController>()
        configurationController.setFromLoginPage(true)
        HelloApplication.primary.scene = scene
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        HelloApplication.primary.isResizable = false
    }
}
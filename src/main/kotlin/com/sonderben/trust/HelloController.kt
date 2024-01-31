package com.sonderben.trust

import com.sonderben.trust.db.dao.EmployeeDao
import javafx.beans.property.SimpleObjectProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import java.net.URL
import java.util.*

class HelloController : Initializable{
    @FXML
    private lateinit var login: Button
    @FXML
    private lateinit var password: PasswordField

    @FXML
    private lateinit var userNameTextField: TextField
    @FXML
    private lateinit var infoLabel: Label

    @FXML
    fun onLoginButtonClick(event: ActionEvent?) {
        val employeeEntity = EmployeeDao.login(userNameTextField.text.trim(),password.text.trim())
        if ( employeeEntity != null ){
            Context.currentEmployee = SimpleObjectProperty(employeeEntity)
            val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/main_view.fxml"))
            val scene = Scene(fxmlLoader.load(), 920.0, 640.0)
            HelloApplication.primary.scene = scene
        }else{
            infoLabel.text = "username or password is wrong"
        }


    }

    @FXML
    fun onCreateNewSystemMouseClicked(event: MouseEvent) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("sale.fxml"))
        val scene = Scene(fxmlLoader.load(), 920.0, 640.0)
        HelloApplication.primary.scene = scene
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }
}
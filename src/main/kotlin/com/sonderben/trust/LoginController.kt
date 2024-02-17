package com.sonderben.trust

import com.itextpdf.text.List
import com.sonderben.trust.controller.ConfigurationController
import com.sonderben.trust.db.dao.EmployeeDao
import com.sonderben.trust.db.dao.enterprise.EnterpriseInfoDao
import entity.enterprise.EnterpriseInfo
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.util.StringConverter
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
    private lateinit var enterpriseCB: ChoiceBox<EnterpriseInfo>

    @FXML
    private lateinit var newSystemLabel: Label


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
        enterpriseCB.isVisible = false
        enterpriseCB.isManaged = false
        HelloApplication.primary.isResizable = false
        val employees = EmployeeDao.employees

        if (employees.size>0){
            login.isDisable = false
            newSystemLabel.isDisable = true
        }

        enterpriseCB.selectionModel.selectedItemProperty().addListener(object :ChangeListener<EnterpriseInfo>{
            override fun changed(p0: ObservableValue<out EnterpriseInfo>?, p1: EnterpriseInfo?, newValue: EnterpriseInfo?) {
                if (newValue != null){
                    login.isDisable = false
                }
            }
        })

        enterpriseCB.converter = EnterpriseStringConverter()
        //enterpriseCB.items = enterPrisesInfos
    }

    class  EnterpriseStringConverter:StringConverter<EnterpriseInfo>(){
        override fun toString(p0: EnterpriseInfo?): String {
            return p0?.name ?: "Select a Enterprise"
        }

        override fun fromString(p0: String?): EnterpriseInfo {
            return EnterpriseInfo()
        }
    }
}
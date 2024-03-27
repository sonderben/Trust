package com.sonderben.trust

import com.sonderben.trust.controller.ConfigurationController
import com.sonderben.trust.db.dao.EmployeeDao
import com.sonderben.trust.viewUtil.ViewUtil
import entity.EmployeeEntity
import entity.enterprise.EnterpriseInfo
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.util.StringConverter
import java.net.URL
import java.util.*
import javax.script.ScriptEngineManager

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
    private lateinit var newSystemLabel: Label
    private val employees: ObservableList<EmployeeEntity> = EmployeeDao.employees


    private lateinit var resourceBundle:ResourceBundle

    @FXML
    fun onLoginButtonClick() {
        Locale.setDefault( Locale.FRENCH )

        val loading = ViewUtil.loadingView()
        loading.show()
        EmployeeDao.login(userNameTextField.text.trim(),password.text.trim())
            .subscribe({employeeEntity ->
                Context.currentEmployee = SimpleObjectProperty(employeeEntity)
                val resourceBundle = ResourceBundle.getBundle("com.sonderben.trust.i18n.string")
                val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/main_view.fxml"),resourceBundle)
                val scene = Scene(fxmlLoader.load(), 720.0, 440.0)
                loading.close()
                HelloApplication.primary.scene = scene
            },{loading.close()},{
                loading.close()
                infoLabel.isVisible = true
                infoLabel.text = "username or password is wrong"
            })


    }

    @FXML
    fun onCreateNewSystemMouseClicked() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/configuration.fxml"),resourceBundle)
        val scene = Scene(fxmlLoader.load(), 920.0, 640.0)
        fxmlLoader.getController<ConfigurationController>()
        HelloApplication.primary.scene = scene
    }
    var isFromEnterprise: Boolean=false
        set(value) {
            field = value
            login.isDisable = false
            newSystemLabel.isDisable = true
        }

    override fun initialize(location: URL?, resources: ResourceBundle) {
        resourceBundle = resources
        val scriptEngineManager = ScriptEngineManager()
        val engineFactories = scriptEngineManager.engineFactories
        val eng = scriptEngineManager.getEngineByExtension("js")
        /*println("3+2= "+ eng.eval("3+2") )

        for (en in engineFactories){
            println("name: ${en.engineName}")
            println("engineVersion: ${en.engineVersion}")
            println("languageVersion: ${en.languageVersion}")
            println("names: ${en.names}")
            println("mimeTypes: ${en.mimeTypes}")
        }*/

        employees.addListener(ListChangeListener {
            if (employees.size >0 ){
                login.isDisable = false
                newSystemLabel.isDisable = true
            }
        })

        if (employees.size>0 || isFromEnterprise){
            login.isDisable = false
            newSystemLabel.isDisable = true
        }


    }


}
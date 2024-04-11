package com.sonderben.trust

import com.sonderben.trust.controller.enterprise.EnterpriseController
import com.sonderben.trust.db.service.AuthentificationService
import com.sonderben.trust.db.service.EnterpriseService
import com.sonderben.trust.viewUtil.ViewUtil
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
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



    private lateinit var resourceBundle:ResourceBundle



    @FXML
    fun onLoginButtonClick() {
        Locale.setDefault( Locale.FRENCH )

        val loading = ViewUtil.loadingView()
        loading.show()
        AuthentificationService.getInstance().login(userNameTextField.text.trim(),password.text.trim())
            .subscribe({employeeEntity ->

                Context.currentEmployee = SimpleObjectProperty(employeeEntity)

                val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/main_view.fxml"),resourceBundle)

                val scene = Scene(fxmlLoader.load(), HelloApplication.primary.scene.width, HelloApplication.primary.scene.height )
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
        val scene = Scene(fxmlLoader.load(),  HelloApplication.primary.scene.width, HelloApplication.primary.scene.height )
        fxmlLoader.getController<EnterpriseController>()
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



        if (EnterpriseService.getInstance().entities.size > 0 /*|| isFromEnterprise*/){
            login.isDisable = false
            newSystemLabel.isDisable = true
        }


    }


}
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
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
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
        if (EnterpriseService.getInstance().entities.size > 0 ){
            val loading = ViewUtil.loadingView()
            loading.show()
            AuthentificationService.getInstance().login(userNameTextField.text.trim(),password.text.trim())
                .subscribe({employeeEntity ->

                    Context.currentEmployee = SimpleObjectProperty(employeeEntity)

                    val cal = Calendar.getInstance()
                    val day =cal.get( Calendar.DAY_OF_WEEK )
                    println("day of week: $day: ${employeeEntity.schedules}")
                    val schedules = employeeEntity.schedules.filter { it.workDay + 1 == day }

                    if( schedules.isNotEmpty() || employeeEntity.isAdmin){
                        val hour = "${cal.get( Calendar.HOUR_OF_DAY )}.${cal.get( Calendar.MINUTE )}".toFloat()
                        val r = schedules.filter { it.startHour.toFloat()>=hour && hour <= it.endHour.toFloat() }
                        println("hour: $hour ")
                        if(employeeEntity.isAdmin || r.isNotEmpty() ){
                            val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/main_view.fxml"),resourceBundle)

                            val scene = Scene(fxmlLoader.load(), HelloApplication.primary.scene.width, HelloApplication.primary.scene.height )
                            loading.close()
                            HelloApplication.primary.scene = scene
                        }else{
                            loading.close()
                            ViewUtil.customAlert(ViewUtil.INFO," u pa gen akse ak sistem nn le sa a").show()
                        }
                    }else{
                        loading.close()
                        ViewUtil.customAlert(ViewUtil.INFO," u pa gen akse ak sistem nn jodi a").show()

                    }



                },{loading.close()},{
                    loading.close()
                    infoLabel.isVisible = true
                    infoLabel.text = "username or password is wrong"
                })
        }
        else{
            ViewUtil.Companion.customAlert(ViewUtil.INFO,resourceBundle.getString("please_create_system")).show()
        }
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



        if (EnterpriseService.getInstance().entities.size > 0 ){
            login.isDisable = false
            newSystemLabel.isDisable = true
        }


    }

    fun passwordOnReleased(keyEvent: KeyEvent) {
        if (keyEvent.code.equals( KeyCode.ENTER ))
            onLoginButtonClick()
    }

    fun userNameOnReleased(keyEvent: KeyEvent) {
        if (keyEvent.code.equals( KeyCode.ENTER ))
            password.requestFocus()
    }


}
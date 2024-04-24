package com.sonderben.trust.controller

import com.sonderben.trust.Context
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.changeVisibility
import com.sonderben.trust.constant.Constant
import com.sonderben.trust.db.service.EmployeeService
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.paint.Color
import javafx.util.Callback
import java.io.IOException
import java.net.URL
import java.util.*

class EditCredentialDialog(val w:Double):Dialog<Boolean>(),Initializable {

    private var isSave = false
    private val resource = Constant.resource
    init {




        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/editCredentialDialog.fxml"),Constant.resource)
        fxmlLoader.setController(this)
        try {
            dialogPane = fxmlLoader.load()
            
            val cancel = ButtonType(resource.getString("cancel"), ButtonBar.ButtonData.APPLY)
            dialogPane.buttonTypes.addAll( cancel )

            resultConverter = Callback { param  ->

                return@Callback isSave
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }




    }
    override fun initialize(p0: URL?, p1: ResourceBundle?) {

        labelInfo.text = ""
        username.text = Context.currentEmployee.get().userName
        if(Context.currentEmployee.get().userName.equals("root")){
            username.isEditable = false
        }
    }
    @FXML lateinit var currentPwd:PasswordField
    @FXML lateinit var newPwd:PasswordField
    @FXML lateinit var confirmPwd:PasswordField
    @FXML lateinit var username:TextField
    @FXML lateinit var labelInfo:Label

    @FXML lateinit var save:Button


    @FXML
    fun saveBtn(){
        if ( validateCredential() ){

            EmployeeService.getInstance().updateCredential( username.text, confirmPwd.text )
                .subscribe(
                    {
                        isSave = true
                        vali()
                        val employee = Context.currentEmployee.get()
                        employee.userName = username.text
                        employee.password = confirmPwd.text

                    },{th-> println(th.message) }
                )

        }
    }


    private fun validateCredential(): Boolean {
        labelInfo.textFill = Color.ORANGE
        if (!currentPwd.text.equals(Context.currentEmployee.get().password)){
            labelInfo.text = resource.getString("current_pwd_wrong")
            return false
        }
        if ( currentPwd.text.isBlank() || newPwd.text.isBlank() || confirmPwd.text.isBlank() ){
            labelInfo.text = resource.getString("fill_all_text_fields")
            return false
        }
        if ( !newPwd.text.equals( confirmPwd.text ) ){
            labelInfo.text = resource.getString("new_pwd_different_with_confirmation_pwd")
            return false
        }
        labelInfo.text = ""
        return true
    }

    private fun vali() {
        labelInfo.textFill = Color.LIGHTGREEN
        labelInfo.text = resource.getString("Credential_update_success")
        newPwd.changeVisibility()
        currentPwd.changeVisibility()
        confirmPwd.changeVisibility()
        username.changeVisibility()
        save.changeVisibility()

    }
}
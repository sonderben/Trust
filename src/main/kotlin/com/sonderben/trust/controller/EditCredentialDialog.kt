package com.sonderben.trust.controller

import com.sonderben.trust.Context
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.changeVisibility
import com.sonderben.trust.db.dao.EmployeeDao
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.paint.Color
import java.io.IOException
import java.net.URL
import java.util.*

class EditCredentialDialog(val w:Double):Dialog<Boolean>(),Initializable {

    init {




        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/EditCredentialDialog.fxml"))
        fxmlLoader.setController(this)
        try {
            dialogPane = fxmlLoader.load()
            
            val save = ButtonType("Ok", ButtonBar.ButtonData.APPLY)
            dialogPane.buttonTypes.addAll( ButtonType("Ok", ButtonBar.ButtonData.OK_DONE) )

            /*setResultConverter { button->
                return true
            }*/



        } catch (e: IOException) {
            e.printStackTrace()
        }




    }
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        username.text = Context.currentEmployee.get().userName
    }
    @FXML lateinit var currentPwd:PasswordField
    @FXML lateinit var newPwd:PasswordField
    @FXML lateinit var confirmPwd:PasswordField
    @FXML lateinit var username:TextField
    @FXML lateinit var labelInfo:Label

    @FXML lateinit var save:Button
    @FXML lateinit var cancel:Button

    @FXML
    fun saveBtn(){
        if ( validateCredential() ){

            EmployeeDao.updateCredential( username.text, confirmPwd.text )
                .subscribe(
                    {
                        vali()
                        val employee = Context.currentEmployee.get()
                        employee.userName = username.text
                        employee.password = confirmPwd.text

                    },{th-> println(th.message) }
                )

        }
    }
    @FXML
    fun cancelBtn(){
        this.close()
    }

    private fun validateCredential(): Boolean {
        labelInfo.textFill = Color.ORANGE
        if (!currentPwd.text.equals(Context.currentEmployee.get().password)){
            labelInfo.text = "The current password is wrong"
            return false
        }
        if ( currentPwd.text.isBlank() || newPwd.text.isBlank() || confirmPwd.text.isBlank() ){
            labelInfo.text = "Please make sure you fill out all the text fields."
            return false
        }
        if ( !newPwd.text.equals( confirmPwd.text ) ){
            labelInfo.text = "The new password is different with the confirmation password."
            return false
        }
        labelInfo.text = ""
        return true
    }

    private fun vali() {
        labelInfo.textFill = Color.LIGHTGREEN
        labelInfo.text = "Credential update with success."
        newPwd.changeVisibility()
        currentPwd.changeVisibility()
        confirmPwd.changeVisibility()
        username.changeVisibility()
        save.changeVisibility()
        cancel.changeVisibility()
    }
}
package com.sonderben.trust.controller.config

import entity.EnterpriseEntity
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.VBox
import java.net.URL
import java.time.LocalDate
import java.util.*

class Admin :Initializable {
    lateinit var phoneTextField: TextField
    var enterprise: EnterpriseEntity?=null
         set(value) {
             field=value
             setEnterprise()
         }
    override fun initialize(p0: URL?, p1: ResourceBundle?) {

        setEnterprise()
        setBusiness()



    }



    private fun setEnterprise() {
       if (enterprise!=null){
           val emp = enterprise!!.employee
           accountNumberTextField.text = emp.bankAccount
           val cal = emp.birthDay
           birthdayDatePicker.value = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

           if (emp.genre!=null){
               choiceBoxGender.selectionModel.select( choiceBoxGender.items.indexOf( emp.genre.lowercase().replaceFirstChar { it.uppercase() } ) )

           }
           choiceBoxRole.items.add(emp.role.name)
           choiceBoxRole.selectionModel.select(0)
           choiceBoxRole.isDisable = true
           userNameTextField.text = emp.userName
           directionField.text = emp.direction
           emailTextField.text = emp.email
           firstNameTextField.text = emp.firstName
           lastNameTextField.text = emp.lastName
           passportTextField.text = emp.passport
           passwordField.text = emp.password
           phoneTextField.text = emp.telephone
           scheduleTextField.text = "Mon-Mon"
       }

    }

    fun onUpdateButton() {

    }

    fun onDeleteButton(actionEvent: ActionEvent) {

    }

    fun onSaveButton() {

    }

    fun scheduleOnMOuseClick() {

    }




    @FXML
     lateinit var accountNumberTextField: TextField

    @FXML
     lateinit var birthdayDatePicker: DatePicker

    @FXML
     lateinit var bottomPanelVBOx: VBox

    @FXML
     lateinit var choiceBoxGender: ChoiceBox<String>

    @FXML
     lateinit var choiceBoxRole: ChoiceBox<String>

    @FXML
     lateinit var deleteButton: Button

    @FXML
     lateinit var directionField: TextField

    @FXML
     lateinit var emailTextField: TextField

    @FXML
     lateinit var firstNameTextField: TextField

    @FXML
     lateinit var lastNameTextField: TextField

    @FXML
     lateinit var passportTextField: TextField

    @FXML
     lateinit var passwordField: PasswordField

    @FXML
     lateinit var saveButton: Button

    @FXML
     lateinit var scheduleTextField: TextField

    @FXML
     lateinit var telephoneTextField: TextField

    @FXML
     lateinit var updateButton: Button

    @FXML
     lateinit var userNameTextField: TextField
     ///////---------------\\\\\\\\\\\\\\\\\\\

    @FXML
    lateinit var categoryChoiceBox: ChoiceBox<String>

    @FXML
    lateinit var directionTextField: TextField

    @FXML
    lateinit var foundationDatePicker: DatePicker

    @FXML
    lateinit var nameTextField: TextField

    /*@FXML
    lateinit var telephoneTextField: TextField*/

    @FXML
    lateinit var websiteTextField: TextField

    private fun setBusiness() {

        if (enterprise!=null){
            directionTextField.text = enterprise!!.direction
            val cal = enterprise!!.foundation
            foundationDatePicker.value = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

            if (enterprise!!.category!=null){
                val cat = enterprise!!.category.name.lowercase().replaceFirstChar { it.uppercaseChar() }
                categoryChoiceBox.selectionModel.select( categoryChoiceBox.items.indexOf( cat ) )
            }

            nameTextField.text = enterprise!!.name
            telephoneTextField.text = enterprise!!.telephone
            websiteTextField.text = enterprise!!.website
        }

    }
}
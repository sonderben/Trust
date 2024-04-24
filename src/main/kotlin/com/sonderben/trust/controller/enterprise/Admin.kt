package com.sonderben.trust.controller.enterprise

import com.sonderben.trust.controller.BaseController
import entity.EnterpriseEntity
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.VBox
import java.net.URL
import java.time.LocalDate
import java.util.*

class Admin :Initializable,BaseController() {
    lateinit var phoneTextField: TextField
    private var bundle:ResourceBundle?=null;
    var enterprise: EnterpriseEntity?=null
         set(value) {
             field=value
             setEnterprise()
             setBusiness(bundle!!)
         }
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        disableQueryControlButton()
        bundle = p1

        categoryChoiceBox.items.clear()
        categoryChoiceBox.items.addAll(
            p1?.getString("supermarket") ?: "Supermarket",
            p1?.getString("hardware_store") ?: "Hardware store",
            p1?.getString("pharmacy") ?: "Pharmacy",
            p1?.getString("general") ?: "General"
        )
    }



    /**
    *set TextField,ChoiceBox,DatePicker (Enterprise part) when enterprise different to null
     */
    private fun setEnterprise() {
       if (enterprise!=null){
           val emp = enterprise!!.adminEntity

           val cal = emp.birthDay
           birthdayDatePicker.value = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

           if (emp.genre!=null){
               choiceBoxGender.selectionModel.select( choiceBoxGender.items.indexOf( emp.genre.lowercase().replaceFirstChar { it.uppercase() } ) )

           }
           choiceBoxRole.items.add(emp.role.name)
           choiceBoxRole.selectionModel.select(0)
           choiceBoxRole.isDisable = true
           userNameTextField.text = emp.userName

           emailTextField.text = emp.email
           firstNameTextField.text = emp.firstName
           lastNameTextField.text = emp.lastName
           passwordField.text = emp.password
           phoneTextField.text = emp.telephone
           scheduleTextField.text = "Mon-Mon"
       }
    }








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
     lateinit var emailTextField: TextField

    @FXML
     lateinit var firstNameTextField: TextField

    @FXML
     lateinit var lastNameTextField: TextField



    @FXML
     lateinit var passwordField: PasswordField

    @FXML
     lateinit var saveButton: Button

    @FXML
     lateinit var scheduleTextField: TextField

    @FXML
     lateinit var telephoneTextField: TextField



    @FXML
     lateinit var userNameTextField: TextField


    @FXML
    lateinit var categoryChoiceBox: ChoiceBox<String>

    @FXML
    lateinit var directionTextField: TextField

    @FXML
    lateinit var foundationDatePicker: DatePicker

    @FXML
    lateinit var nameTextField: TextField



    @FXML
    lateinit var websiteTextField: TextField

    /**
     *set TextField,ChoiceBox,DatePicker (business part) when enterprise different to null
     */
    private fun setBusiness( bundle:ResourceBundle ) {

        if (enterprise!=null){
            directionTextField.text = enterprise!!.direction
            val cal = enterprise!!.foundation
            foundationDatePicker.value = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

            if (enterprise!!.category!=null){
                val keyI18nCat = enterprise!!.category.name.lowercase().replace(" ","_")
                categoryChoiceBox.selectionModel.select( categoryChoiceBox.items.indexOf( bundle.getString( keyI18nCat ) ) )
            }

            nameTextField.text = enterprise!!.name
            telephoneTextField.text = enterprise!!.telephone
            websiteTextField.text = enterprise!!.website
        }

    }

    override fun onDestroy() {

    }
}
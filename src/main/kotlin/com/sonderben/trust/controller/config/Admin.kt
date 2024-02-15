package com.sonderben.trust.controller.config

import com.sonderben.trust.controller.BaseController
import com.sonderben.trust.db.dao.EnterpriseDao
import entity.EnterpriseEntity
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import java.net.URL
import java.time.LocalDate
import java.util.*

class Admin :Initializable {
    private val enterprise: ObservableList<EnterpriseEntity> = EnterpriseDao.enterprises
    override fun initialize(p0: URL?, p1: ResourceBundle?) {

        setEnterprise()

        enterprise.addListener(ListChangeListener { setEnterprise() })

    }

    private fun setEnterprise() {
        if (enterprise.size>0){
            val emp = enterprise[0].employee
            accountNumberTextField.text = emp.bankAccount
            val cal = emp.birthDay
            birthdayDatePicker.value = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

            choiceBoxGender.selectionModel.select( choiceBoxGender.items.indexOf( emp.genre.lowercase().replaceFirstChar { it.uppercase() } ) )
            choiceBoxRole.items.add(emp.role.name)
            choiceBoxGender.selectionModel.select(emp.role.name)
            userNameTextField.text = emp.userName
            directionField.text = emp.direction
            emailTextField.text = emp.email
            firstNameTextField.text = emp.firstName
            lastNameTextField.text = emp.lastName
            passportTextField.text = emp.passport
            passwordField.text = emp.password
            telephoneTextField.text = emp.telephone
            scheduleTextField.text = "Mon-Mon"
        }
    }

    fun onUpdateButton(actionEvent: ActionEvent) {

    }

    fun onDeleteButton(actionEvent: ActionEvent) {

    }

    fun onSaveButton(actionEvent: ActionEvent) {

    }

    fun scheduleOnMOuseClick(mouseEvent: MouseEvent) {

    }




    @FXML
    private lateinit var accountNumberTextField: TextField

    @FXML
    private lateinit var birthdayDatePicker: DatePicker

    @FXML
    private lateinit var bottomPanelVBOx: VBox

    @FXML
    private lateinit var choiceBoxGender: ChoiceBox<String>

    @FXML
    private lateinit var choiceBoxRole: ChoiceBox<String>

    @FXML
    private lateinit var deleteButton: Button

    @FXML
    private lateinit var directionField: TextField

    @FXML
    private lateinit var emailTextField: TextField

    @FXML
    private lateinit var firstNameTextField: TextField

    @FXML
    private lateinit var lastNameTextField: TextField

    @FXML
    private lateinit var passportTextField: TextField

    @FXML
    private lateinit var passwordField: PasswordField

    @FXML
    private lateinit var saveButton: Button

    @FXML
    private lateinit var scheduleTextField: TextField

    @FXML
    private lateinit var telephoneTextField: TextField

    @FXML
    private lateinit var updateButton: Button

    @FXML
    private lateinit var userNameTextField: TextField
}
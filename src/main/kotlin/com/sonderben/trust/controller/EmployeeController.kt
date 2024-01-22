package com.sonderben.trust.controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import java.net.URL
import java.util.*

class EmployeeController: Initializable {
    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

    @FXML
    private lateinit var GenreCol: TableColumn<Any, Any>

    @FXML
    private lateinit var PassportCol: TableColumn<Any, Any>

    @FXML
    private lateinit var RolesCol: TableColumn<Any, Any>

    @FXML
    private lateinit var accountNumberCol: TableColumn<Any, Any>

    @FXML
    private lateinit var accountNumberTextField: TextField

    @FXML
    private lateinit var birthdayCol: TableColumn<Any, Any>

    @FXML
    private lateinit var birthdayDatePicker: DatePicker

    @FXML
    private lateinit var choiceBoxGender: ChoiceBox<Any>

    @FXML
    private lateinit var choiceBoxRole: ChoiceBox<Any>

    @FXML
    private lateinit var countCol: TableColumn<Any, Any>

    @FXML
    private lateinit var deleteButton: Button

    @FXML
    private lateinit var directionCol: TableColumn<Any, Any>

    @FXML
    private lateinit var directionField: TextField

    @FXML
    private lateinit var emailCol: TableColumn<Any, Any>

    @FXML
    private lateinit var emailTextField: TextField

    @FXML
    private lateinit var firstNameCol: TableColumn<Any, Any>

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
    private lateinit var scheduleCol: TableColumn<Any, Any>

    @FXML
    private lateinit var telephoneCol: TableColumn<Any, Any>

    @FXML
    private lateinit var telephoneTextField: TextField

    @FXML
    private lateinit var updateButton: Button

    @FXML
    private lateinit var userNameCol: TableColumn<Any, Any>

    @FXML
    private lateinit var userNameTextField: TextField

    @FXML
    private lateinit var userTableView: TableView<Any>

    @FXML
    fun onDeleteButton(event: ActionEvent) {

    }

    @FXML
    fun onSaveButton(event: ActionEvent) {

    }

    @FXML
    fun onUpdateButton(event: ActionEvent) {

    }
}
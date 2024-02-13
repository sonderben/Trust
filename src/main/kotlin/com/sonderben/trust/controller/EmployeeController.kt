package com.sonderben.trust.controller

import com.sonderben.trust.HelloApplication
import com.sonderben.trust.Util
import com.sonderben.trust.db.dao.EmployeeDao
import com.sonderben.trust.db.dao.RoleDao
import com.sonderben.trust.format
import com.sonderben.trust.hide
import com.sonderben.trust.model.Role
import entity.EmployeeEntity
import entity.ScheduleEntity
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.util.Callback
import javafx.util.StringConverter
import java.net.URL
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class EmployeeController: Initializable {
    var days = arrayListOf<String>("Lunes","Martes","Miercroles","Jueves","Viernes","Sabado","Domingo")

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        GenreCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.genre) }
        birthdayCol.setCellValueFactory { employee -> SimpleStringProperty( employee.value.birthDay.format() ) }
        firstNameCol.setCellValueFactory { employee -> SimpleStringProperty("${employee.value.fullName}") }
        directionCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.direction) }
        emailCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.email) }
        accountNumberCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.bankAccount) }
        telephoneCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.telephone) }
        userNameCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.userName) }
        RolesCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.role.name ) }

        PassportCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.passport) }

        scheduleCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.schedules.joinToString { schedule ->days[schedule.workDay].substring(0,2)  }) }


        userTableView.items = EmployeeDao.employees

        choiceBoxRole.items = RoleDao.roles
        choiceBoxRole.converter = RoleStringConverter()

        userTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        userTableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                if (!bottomPanelVBOx.isVisible) {
                    bottomPanelVBOx.hide()
                }

                employeeToSave = newValue
                telephoneTextField.text = newValue.telephone
                lastNameTextField.text = newValue.lastName
                emailTextField.text = newValue.email
                firstNameTextField.text = newValue.firstName
                directionField.text = newValue.direction
                choiceBoxGender.selectionModel.select(choiceBoxGender.items.indexOf(newValue.genre))
                choiceBoxRole.selectionModel.select(choiceBoxRole.items.indexOf(newValue.role))
                accountNumberTextField.text = newValue.bankAccount
                userNameTextField.text = newValue.userName
                telephoneTextField.text = newValue.telephone
                passwordField.text = "********"
                telephoneTextField.text = newValue.telephone
                passportTextField.text = newValue.passport
                val cal = newValue.birthDay
                birthdayDatePicker.value =
                    LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
                scheduleTextField.text = newValue.schedules.joinToString(separator = ", ") { scheduleEntity ->
                    days[scheduleEntity.workDay].substring(
                        0,
                        2
                    )
                }
            }
        }


    }

    @FXML
    private lateinit var GenreCol: TableColumn<EmployeeEntity, String>
    private var employeeToSave = EmployeeEntity()

    @FXML
    private lateinit var PassportCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var RolesCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var accountNumberCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var accountNumberTextField: TextField

    @FXML
    private lateinit var birthdayCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var birthdayDatePicker: DatePicker

    @FXML
    private lateinit var choiceBoxGender: ChoiceBox<String>

    @FXML
    private lateinit var choiceBoxRole: ChoiceBox<Role>

    @FXML
    private lateinit var countCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var deleteButton: Button

    @FXML
    private lateinit var directionCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var directionField: TextField

    @FXML
    private lateinit var emailCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var emailTextField: TextField

    @FXML
    private lateinit var firstNameCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var firstNameTextField: TextField

    @FXML
    private lateinit var lastNameTextField: TextField

    @FXML
    private lateinit var scheduleTextField: TextField

    @FXML
    private lateinit var passportTextField: TextField

    @FXML
    private lateinit var passwordField: PasswordField

    @FXML
    private lateinit var saveButton: Button

    @FXML
    private lateinit var scheduleCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var telephoneCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var telephoneTextField: TextField

    @FXML
    private lateinit var updateButton: Button

    @FXML
    private lateinit var userNameCol: TableColumn<EmployeeEntity, String>

    @FXML
    private lateinit var userNameTextField: TextField

    @FXML
    private lateinit var userTableView: TableView<EmployeeEntity>

    @FXML
    fun onDeleteButton(event: ActionEvent) {
        if (employeeToSave.id != null){
            EmployeeDao.delete( employeeToSave.id )
            clear()
            userTableView.selectionModel.select(null)
        }

    }

    @FXML
    fun onSaveButton(event: ActionEvent) {

         employeeToSave.apply {
             firstName = firstNameTextField.text
             passport = passportTextField.text
             lastName = lastNameTextField.text
             genre = choiceBoxGender.value
             direction = directionField.text
             email = emailTextField.text
             telephone = telephoneTextField.text
             birthDay = GregorianCalendar.from(birthdayDatePicker.value.atStartOfDay(ZoneId.systemDefault()))
             bankAccount = accountNumberTextField.text
             userName = userNameTextField.text
             password = passwordField.text
             role = choiceBoxRole.value
             /*mutableListOf()*/
         }
        EmployeeDao.save(employeeToSave)

        clear()
        userTableView.selectionModel.select(null)
    }

    @FXML
    fun onUpdateButton(event: ActionEvent) {
        userTableView.selectionModel.select(null)
    }
    @FXML
    private lateinit var bottomPanelVBOx:VBox
    @FXML
    fun scheduleOnMOuseClick(event: MouseEvent) {
        val scheduleEntity = SchedulerController( employeeToSave.schedules?: mutableListOf() ).showAndWait()
        println("$scheduleEntity : genial")
        employeeToSave.schedules = scheduleEntity.get()
        scheduleTextField.text = employeeToSave.schedules.joinToString(separator = ", "){ schedule->days[schedule.workDay].substring(0,2) }
    }

    private fun clear(){
        firstNameTextField.text = ""
        lastNameTextField.text = ""
        passportTextField.text = ""
        directionField.text = ""
        emailTextField.text = ""
        telephoneTextField.text = ""
        accountNumberTextField.text = ""
        userNameTextField.text = ""
        passwordField.text = ""
    }

    fun hideBottomPanelOnMouseClicked(mouseEvent: MouseEvent) {
        bottomPanelVBOx.hide()
    }

    class RoleStringConverter: StringConverter<Role>() {
        override fun toString(`object`: Role?): String {
            return `object`?.name ?: "Select a Role"
        }

        override fun fromString(string: String?): Role? {
            return null
        }

    }

    class SchedulerController(schedulers:List<ScheduleEntity>): Dialog<List<ScheduleEntity>>() {
        var scheduleLoader = FXMLLoader(HelloApplication.javaClass.getResource("view/scheduler.fxml"))
        init {
            this.initOwner(HelloApplication.primary)
            scheduleLoader.setController(this)
            val buttonType = ButtonType("Ok", ButtonBar.ButtonData.OK_DONE)
            dialogPane = scheduleLoader.load()
            dialogPane.buttonTypes.addAll( buttonType )

            dayCol.setCellValueFactory { data -> SimpleStringProperty( dayChoicebox.items[data.value.workDay] ) }
            startHourCol.setCellValueFactory { data -> SimpleStringProperty(data.value.startHour.toString()) }
            endHourCol.setCellValueFactory { data -> SimpleStringProperty(data.value.endHour.toString()) }
            tableView.items = FXCollections.observableArrayList(schedulers)

            setResultConverter(object:Callback<ButtonType,List<ScheduleEntity>>{
                override fun call(param: ButtonType?): List<ScheduleEntity> {

                    return tableView.items
                }
            })
        }

        @FXML
        private lateinit var dayChoicebox: ChoiceBox<String>

        @FXML
        private lateinit var dayCol: TableColumn<ScheduleEntity, String>

        @FXML
        private lateinit var endHourCol: TableColumn<ScheduleEntity, String>

        @FXML
        private lateinit var endHourTf: TextField

        @FXML
        private lateinit var startHourCol: TableColumn<ScheduleEntity, String>

        @FXML
        private   lateinit var  startHourTf: TextField

        @FXML
        private lateinit var tableView: TableView<ScheduleEntity>



        @FXML
        fun onDelete(event: ActionEvent?) {
        }

        @FXML
        fun onSave(event: ActionEvent?) {
            val schedule = ScheduleEntity(
                null,
                dayChoicebox.items.indexOf(dayChoicebox.value),
                startHourTf.text.toFloat(),endHourTf.text.toFloat()
            )
            tableView.items.add( schedule )


        }

        @FXML
        fun onUpdate(event: ActionEvent?) {
        }

    }
}
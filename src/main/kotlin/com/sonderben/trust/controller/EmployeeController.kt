package com.sonderben.trust.controller

import com.sonderben.trust.*
import com.sonderben.trust.constant.Constant
import com.sonderben.trust.db.service.EmployeeService

import com.sonderben.trust.db.service.RoleService
import com.sonderben.trust.db.service.ScheduleService
import com.sonderben.trust.viewUtil.ViewUtil
import entity.EmployeeEntity
import entity.Role
import entity.ScheduleEntity
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*

import javafx.scene.layout.VBox
import javafx.util.Callback
import javafx.util.StringConverter
import java.net.URL
import java.util.*


class EmployeeController : Initializable, BaseController() {
    @FXML
    lateinit var mainPane: VBox
    lateinit var days:ArrayList<String>
    lateinit var resources: ResourceBundle


    override fun initialize(location: URL?, resourceBundle: ResourceBundle) {
        resources=resourceBundle
        disableQueryControlButton()
        editMenuItem()
        days = resources.getString("days").split(",") as ArrayList<String>

        telephoneTextField.onlyInt()

        userTableView.selectionModel.selectedIndices.addListener(ListChangeListener { change ->
            if (change.list.isEmpty()) {
                enableActionButton(mainPane, true)
            } else {
                enableActionButton(mainPane, false)
            }
        })

        MainController.hideBottomBar(false) {
            hideBottomPanelOnMouseClicked()
        }

        GenreCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.genre) }
        birthdayCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.birthDay.format()) }
        firstNameCol.setCellValueFactory { employee -> SimpleStringProperty("${employee.value.fullName}") }
        directionCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.direction) }
        emailCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.email) }
        accountNumberCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.bankAccount) }
        telephoneCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.telephone) }
        userNameCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.userName) }
        RolesCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.role.name) }

        PassportCol.setCellValueFactory { employee -> SimpleStringProperty(employee.value.passport) }

        scheduleCol.setCellValueFactory { employee ->
            SimpleStringProperty(employee.value.schedules.joinToString { schedule ->
                days[schedule.workDay].substring(
                    0,
                    2
                )
            })
        }


        userTableView.items = EmployeeService.getInstance().entities

        choiceBoxRole.items = RoleService.getInstance().entities
        choiceBoxRole.converter = RoleStringConverter()




        userTableView.setRowFactory {
            val row = TableRow<EmployeeEntity>()
            val roles = RoleService.getInstance().entities

            row.itemProperty().addListener { _, _, newValue ->
                if (newValue != null && !roles.contains(newValue.role)) {
                    row.isDisable = true
                    row.opacity = 0.5
                }
            }


            row

        }




        userTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        userTableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->


            //is authorized to change this employee
            if (newValue != null && RoleService.getInstance().entities.contains(newValue.role)) {

                if (!bottomPanelVBOx.isVisible) {
                    bottomPanelVBOx.changeVisibility()
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
                passwordField.text = newValue.password //"********"
                telephoneTextField.text = newValue.telephone
                passportTextField.text = newValue.passport

                birthdayDatePicker.value = newValue.birthDay.toLocalDate()

                //scheduleEntity.


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
    private var employeeToSave: EmployeeEntity? = null

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
    fun onDeleteButton() {
        if (employeeToSave != null && employeeToSave!!.id != null) {
            EmployeeService.getInstance().delete(employeeToSave!!.id).subscribe(
                {
                    clear(mainPane)
                    userTableView.selectionModel.select(null)
                }, { th -> println(th.message) })

        }

    }

    @FXML
    fun onSaveButton() {
        //employeeToSave = EmployeeEntity()

        if (validateEmployee()) {
            employeeToSave!!.apply {
                firstName = firstNameTextField.text
                passport = passportTextField.text
                lastName = lastNameTextField.text
                genre = choiceBoxGender.value
                direction = directionField.text
                email = emailTextField.text
                telephone = telephoneTextField.text
                birthDay = birthdayDatePicker.value.toCalendar()
                bankAccount = accountNumberTextField.text
                userName = userNameTextField.text
                password = passwordField.text
                role = choiceBoxRole.value
                /*mutableListOf()*/
            }
            EmployeeService.getInstance().save(employeeToSave!!).subscribe({
                clear(mainPane)
                userTableView.selectionModel.select(null)
            }, { th -> println(th.message) })
        }


    }

    private fun validateEmployee(): Boolean {
        try {

            if (employeeToSave == null || employeeToSave!!.schedules.isEmpty()) {
                ViewUtil.customAlert(ViewUtil.WARNING, resources.getString("please_add_scheduler"))
                    .showAndWait()
                return false
            }


            if (firstNameTextField.text.isBlank() || passportTextField.text.isBlank() || lastNameTextField.text.isBlank() ||
                choiceBoxGender.value.isBlank() || directionField.text.isBlank() || emailTextField.text.isBlank() ||
                telephoneTextField.text.isBlank() || accountNumberTextField.text.isBlank() || userNameTextField.text.isBlank() ||
                passwordField.text.isBlank()
            ) {
                ViewUtil.customAlert(ViewUtil.WARNING, resources.getString("fill_all_text_fields")).show()
                return false
            }

            if (birthdayDatePicker.value == null) {
                ViewUtil.customAlert(ViewUtil.WARNING, resources.getString("please_enter_birthday")).show()
                return false
            }


            if (choiceBoxRole.value == null) {
                ViewUtil.customAlert(ViewUtil.WARNING, resources.getString("please_select_role")).show()
                return false
            }
        } catch (e: Exception) {
            ViewUtil.customAlert(ViewUtil.WARNING, resources.getString("fill_all_text_fields")).show()
            return false
        }

        return true
    }

    @FXML
    fun onUpdateButton() {


        if (employeeToSave != null) {
            if (validateEmployee()) {
                if (employeeToSave!!.schedules == null || employeeToSave!!.schedules.isEmpty()) {
                    ViewUtil.customAlert(ViewUtil.WARNING,  resources.getString("please_add_scheduler"))
                        .showAndWait()
                    return
                }
                employeeToSave!!.apply {
                    firstName = firstNameTextField.text
                    passport = passportTextField.text
                    lastName = lastNameTextField.text
                    genre = choiceBoxGender.value
                    direction = directionField.text
                    email = emailTextField.text
                    telephone = telephoneTextField.text
                    birthDay = birthdayDatePicker.value.toCalendar()
                    bankAccount = accountNumberTextField.text
                    userName = userNameTextField.text
                    password = passwordField.text
                    role = choiceBoxRole.value
                }

                EmployeeService.getInstance().update(employeeToSave!!)
                    .subscribe({
                        clear(mainPane)
                        userTableView.selectionModel.select(null)
                    }, { th -> println(th.message) })
            }
        } else {
            ViewUtil.customAlert(ViewUtil.WARNING,  resources.getString("please_select_employee")).showAndWait()
        }

    }

    @FXML
    private lateinit var bottomPanelVBOx: VBox

    @FXML
    fun scheduleOnMOuseClick() {
        if (employeeToSave == null) {
            employeeToSave = EmployeeEntity()
        }
        val scheduleEntity =
            SchedulerController(employeeToSave!!.schedules).showAndWait()
        employeeToSave!!.schedules = scheduleEntity.get()

        scheduleTextField.text =
            scheduleEntity.get().joinToString(separator = ", ") { schedule -> days[schedule.workDay].substring(0, 2) }
    }


    fun hideBottomPanelOnMouseClicked() {
        bottomPanelVBOx.changeVisibility()
    }

    class RoleStringConverter : StringConverter<Role>() {
        override fun toString(`object`: Role?): String {
            return `object`?.name ?: Constant.resource.getString("select_role")
        }

        override fun fromString(string: String?): Role? {
            return null
        }

    }

    class SchedulerController(var schedulers: List<ScheduleEntity>) : Dialog<List<ScheduleEntity>>(), Initializable {
        private var scheduleLoader =
            FXMLLoader(HelloApplication.Companion::class.java.getResource("view/scheduler.fxml"), Constant.resource)

        init {


            this.initOwner(HelloApplication.primary)
            scheduleLoader.setController(this)
            val buttonType = ButtonType("Ok", ButtonBar.ButtonData.OK_DONE)
            dialogPane = scheduleLoader.load()
            dialogPane.buttonTypes.addAll(buttonType)



            setResultConverter { tableView.items }
        }

        @FXML
        private lateinit var dayChoicebox: ChoiceBox<String>

        @FXML
        private lateinit var dayCol: TableColumn<ScheduleEntity, String>

        @FXML
        private lateinit var endHourCol: TableColumn<ScheduleEntity, String>

        @FXML
        private lateinit var cbStartHour: ChoiceBox<String>

        //cbStartHour
        @FXML
        private lateinit var cbStartMin: ChoiceBox<String>

        @FXML
        private lateinit var cbEndHour: ChoiceBox<String>

        @FXML
        private lateinit var cbEndMin: ChoiceBox<String>

        @FXML
        private lateinit var startHourCol: TableColumn<ScheduleEntity, String>



        @FXML
        private lateinit var tableView: TableView<ScheduleEntity>
        private var selectedScheduler:ScheduleEntity? = null


        @FXML
        fun onDelete() {
             selectedScheduler = tableView.selectionModel.selectedItem
            if (selectedScheduler != null) {

                if (selectedScheduler?.id != null){
                    ScheduleService.getInstance().delete(selectedScheduler!!.id)
                        .subscribe({
                            tableView.items.remove(selectedScheduler)
                            clear()
                        }, { th -> println(th.message) })
                }else{
                    tableView.items.remove(selectedScheduler)
                }

            } else {
                //print() select a schedule first

            }

        }

        @FXML
        fun onSave() {
            val schedule = ScheduleEntity(
                Context.currentEmployee.get().id,
                null,
                dayChoicebox.items.indexOf(dayChoicebox.value),
                "${cbStartHour.value}.${cbStartMin.value}", "${cbEndHour.value}.${cbEndMin.value}"
            )
            if ( tableView.items.any { it.idEmployee.equals( Context.currentEmployee.get().id ) } ) {
                ScheduleService.getInstance().save(
                    schedule
                ).subscribe({  tableView.items.add(schedule);clear() }, { th -> println(th.message) })
            }else{

                tableView.items.add(schedule)
                clear()
            }





        }

        private fun clear() {
            dayChoicebox.selectionModel.clearSelection()

            cbStartHour.selectionModel.clearSelection()
            cbStartMin.selectionModel.clearSelection()

            cbEndHour.selectionModel.clearSelection()
            cbEndMin.selectionModel.clearSelection()


            tableView.selectionModel.clearSelection()
        }

        @FXML
        fun onUpdate() {

            selectedScheduler = tableView.selectionModel.selectedItem

            if (selectedScheduler != null) {

                /*val temp = ScheduleEntity(
                    Context.currentEmployee.get().id,
                    selectedScheduler!!.id,
                    dayChoicebox.items.indexOf(dayChoicebox.value),
                    "${cbStartHour.value}.${cbStartMin.value}",
                    "${cbEndHour.value}.${cbEndMin.value}"
                )*/

                selectedScheduler?.apply {
                    workDay=dayChoicebox.selectionModel.selectedIndex
                    startHour = "${cbStartHour.value}.${cbStartMin.value}"
                    endHour = "${cbEndHour.value}.${cbEndMin.value}"
                }

                    if ( selectedScheduler?.idEmployee != null ){
                        println("dif de ul")
                        ScheduleService.getInstance().update(selectedScheduler!!)
                            .subscribe({

                                tableView.items[tableView.items.indexOf(selectedScheduler)] = selectedScheduler!!
                                clear()

                            }, { th -> println(th.message);clear() })
                    }else{
                        tableView.items[ tableView.items.indexOf(selectedScheduler) ] = selectedScheduler!!
                        println("dif de ulgghfhgfhgfhgh")
                        clear()
                    }

            } else {
                ViewUtil.customAlert(ViewUtil.ERROR,"select a schedule first")
            }
        }

        override fun initialize(location: URL?, resources: ResourceBundle) {

            val days = resources.getString("days").split(",")
            dayChoicebox.items = FXCollections.observableArrayList( days)

            dayCol.setCellValueFactory { data -> SimpleStringProperty( /*dayChoicebox.items[data.value.workDay])*/ days[ data.value.workDay ] )}
            startHourCol.setCellValueFactory { data -> SimpleStringProperty(data.value.startHour.replace(".", "H ")) }
            endHourCol.setCellValueFactory { data -> SimpleStringProperty(data.value.endHour.replace(".","H ")) }
            tableView.items = FXCollections.observableArrayList(schedulers)

            tableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
                 if (newValue!=null){
                     selectedScheduler = newValue

                     dayChoicebox.selectionModel.select( newValue.workDay )


                     cbStartHour.selectionModel.select( newValue.startHour.split(".")[0] )
                     cbStartMin.selectionModel.select( newValue.startHour.split(".")[1] )

                     cbEndHour.selectionModel.select( newValue.endHour.split(".")[0] )
                     cbEndMin.selectionModel.select( newValue.endHour.split(".")[1] )
                 }

            }

            for (i in 0 until 60) {

                if (i < 24) {
                    cbStartHour.items.add("$i".padStart(2, '0'))
                    cbEndHour.items.add("$i".padStart(2, '0'))
                }

                cbStartMin.items.add("$i".padStart(2, '0'))
                cbEndMin.items.add("$i".padStart(2, '0'))
            }

        }

    }

    override fun onDestroy() {

    }

    private fun editMenuItem() {
        MainController.editMenu?.items?.clear()


        val saveMenuItem = MenuItem(resources.getString("save"))
        saveMenuItem.setOnAction {

        }
        val updateMenuItems = MenuItem(resources.getString("update"))
        updateMenuItems.setOnAction {

        }

        val deleteMenuItems = MenuItem(resources.getString("delete"))
        deleteMenuItems.setOnAction {

        }
        val clearMenuItems = MenuItem(resources.getString("clear"))
        clearMenuItems.setOnAction {
            clear(mainPane)
        }


        MainController.editMenu?.items?.addAll(saveMenuItem, updateMenuItems, deleteMenuItems)

    }

    @FXML
    fun clearButton() {
        clear(mainPane)
    }
}
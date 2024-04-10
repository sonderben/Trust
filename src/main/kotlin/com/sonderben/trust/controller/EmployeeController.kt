package com.sonderben.trust.controller

import com.sonderben.trust.*
import com.sonderben.trust.db.service.EmployeeService

import com.sonderben.trust.db.service.RoleService
import com.sonderben.trust.db.service.ScheduleService
import com.sonderben.trust.model.Role
import com.sonderben.trust.viewUtil.ViewUtil
import entity.EmployeeEntity
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


class EmployeeController:Initializable, BaseController() {
    @FXML
    lateinit var mainPane: VBox
    var days = arrayListOf<String>("Lunes","Martes","Miercroles","Jueves","Viernes","Sabado","Domingo")
    //var scheduleEntity: Optional<List<ScheduleEntity>>?  = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        editMenuItem()


        userTableView.selectionModel.selectedIndices.addListener( ListChangeListener { change->
            if ( change.list.isEmpty() ){
                enableActionButton(mainPane,true)
            }
            else{
                enableActionButton(mainPane,false)
            }
        } )

        MainController.hideBottomBar(false) {
            hideBottomPanelOnMouseClicked()
        }

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


        userTableView.items = EmployeeService.getInstance().entities

        choiceBoxRole.items = RoleService.getInstance().entities
        choiceBoxRole.converter = RoleStringConverter()




        userTableView.setRowFactory {
            val row = TableRow<EmployeeEntity>()
            val roles = RoleService.getInstance().entities

            row.itemProperty().addListener { _, _, newValue ->
               if ( newValue != null && !roles.contains( newValue.role ) ){
                   row.isDisable = true
                   row.opacity = 0.5
               }
            }


            row

        }




        userTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        userTableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->


            //is authorized to change this employee
            if (newValue != null && RoleService.getInstance().entities.contains( newValue.role )) {

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
    private var employeeToSave: EmployeeEntity?=null

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
        if ( employeeToSave!=null && employeeToSave!!.id != null){
            EmployeeService.getInstance().delete( employeeToSave!!.id ).subscribe(
                {
                clear( mainPane )
                userTableView.selectionModel.select(null)
            },{th-> println(th.message) })

        }

    }

    @FXML
    fun onSaveButton() {
        //employeeToSave = EmployeeEntity()

         if(validateEmployee()){
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
             },{th-> println(th.message) })
         }


    }

    private fun validateEmployee(): Boolean {
         try {

             if (employeeToSave==null || employeeToSave!!.schedules.isEmpty()){
                 ViewUtil.createAlert(Alert.AlertType.WARNING,"Schedulers is obligatory","Please add Scheduler").showAndWait()
                 return false
             }


             if( firstNameTextField.text.isBlank() || passportTextField.text.isBlank() || lastNameTextField.text.isBlank() ||
                 choiceBoxGender.value.isBlank() || directionField.text.isBlank() || emailTextField.text.isBlank() ||
                 telephoneTextField.text.isBlank() || accountNumberTextField.text.isBlank() || userNameTextField.text.isBlank() ||
                 passwordField.text.isBlank()){
                 ViewUtil.customAlert("Error on fields","please make sure you fill out all the text fields.").show()
                 return false
             }

             if ( birthdayDatePicker.value == null){
                 ViewUtil.customAlert("Error on fields","please enter a valid birthday.").show()
                 return false
             }


             if(choiceBoxRole.value == null){
                 ViewUtil.customAlert("Error on fields","please select a role and try again.").show()
                 return false
             }
         }catch (e:Exception){
             ViewUtil.customAlert("Error on fields","please make sure you fill out all the text fields.").show()
             return false
         }

        return true
    }

    @FXML
    fun onUpdateButton() {


      if (employeeToSave != null){
          if ( validateEmployee() ){
              if ( employeeToSave!!.schedules==null || employeeToSave!!.schedules.isEmpty() ){
                  ViewUtil.createAlert(Alert.AlertType.WARNING,"Schedulers is obligatory","Please add Scheduler").showAndWait()
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

              EmployeeService.getInstance().update( employeeToSave!! )
                  .subscribe({
                      clear(mainPane)
                      userTableView.selectionModel.select(null)
                  },{th-> println(th.message) })
          }
      }else{
          ViewUtil.createAlert(Alert.AlertType.WARNING,"No data","Please select a employee first.").showAndWait()
      }

    }
    @FXML
    private lateinit var bottomPanelVBOx:VBox
    @FXML
    fun scheduleOnMOuseClick() {
        if (employeeToSave==null){
            employeeToSave = EmployeeEntity()
        }
        val scheduleEntity =
            SchedulerController( employeeToSave!!.schedules ).showAndWait()
        employeeToSave!!.schedules = scheduleEntity.get()

        scheduleTextField.text = scheduleEntity.get().joinToString(separator = ", "){ schedule->days[schedule.workDay].substring(0,2) }
    }



    fun hideBottomPanelOnMouseClicked() {
        bottomPanelVBOx.changeVisibility()
    }

    class RoleStringConverter: StringConverter<Role>() {
        override fun toString(`object`: Role?): String {
            return `object`?.name ?: "Select a Role"
        }

        override fun fromString(string: String?): Role? {
            return null
        }

    }

    class SchedulerController(var schedulers:List<ScheduleEntity>): Dialog<List<ScheduleEntity>>() {
        private var scheduleLoader = FXMLLoader(HelloApplication.Companion::class.java.getResource("view/scheduler.fxml"))
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
        fun onDelete() {
            val selectedScheduler = tableView.selectionModel.selectedItem
            if (selectedScheduler.id != null){
                ScheduleService.getInstance().delete( selectedScheduler.id )
                    .subscribe({
                        tableView.items.remove( selectedScheduler )
                        clear(  )
                    },{th-> println(th.message) })
            }else{
                tableView.items.remove( selectedScheduler )
            }

        }

        @FXML
        fun onSave() {
            val schedule = ScheduleEntity(
                Context.currentEmployee.get().id,
                null,
                dayChoicebox.items.indexOf(dayChoicebox.value),
                startHourTf.text.toFloat(),endHourTf.text.toFloat()
            )
            if ( tableView.items.isNotEmpty() && tableView.items.filter { it.idEmployee!=null }.isNotEmpty() ){
                ScheduleService.getInstance().save(
                    ScheduleEntity(
                        Context.currentEmployee.get().id,
                        null,
                        dayChoicebox.items.indexOf(dayChoicebox.value),
                        startHourTf.text.toFloat(),endHourTf.text.toFloat()
                    )
                ).subscribe({ clear() },{th-> println(th.message) })
            }


            tableView.items.add( schedule )


        }

        private fun clear(){
            dayChoicebox.selectionModel.clearSelection()
            startHourTf.text = ""
            endHourTf.text = ""
            tableView.selectionModel.clearSelection()
        }
        @FXML
        fun onUpdate() {
            val selectedScheduler = tableView.selectionModel.selectedItem
            if (selectedScheduler != null){
                if (selectedScheduler.id != null){
                    ScheduleService.getInstance().update(selectedScheduler)
                        .subscribe({
                            clear()
                            tableView.items[ tableView.items.indexOf( selectedScheduler ) ] =
                                ScheduleEntity(
                                    selectedScheduler.idEmployee,selectedScheduler.id,dayChoicebox.items.indexOf(dayChoicebox.value),
                                    startHourTf.text.toFloat(),endHourTf.text.toFloat()
                                )

                        },{th-> println(th.message) })
                }
            }else{
                //select first
            }
        }

    }

    override fun onDestroy() {

    }

    private fun editMenuItem() {
        MainController.editMenu?.items?.clear()


        val saveMenuItem =  MenuItem("Save")
        saveMenuItem.setOnAction {

        }
        val updateMenuItems =  MenuItem("Update")
        updateMenuItems.setOnAction {

        }

        val deleteMenuItems =  MenuItem("Delete")
        deleteMenuItems.setOnAction {

        }
        val clearMenuItems =  MenuItem("Clear")
        clearMenuItems.setOnAction {
            clear(mainPane)
        }


        MainController.editMenu?.items?.addAll(  saveMenuItem,updateMenuItems,deleteMenuItems  )

    }

    @FXML
    fun clearButton() {
        clear(mainPane)
    }
}
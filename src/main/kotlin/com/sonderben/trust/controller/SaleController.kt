package com.sonderben.trust.controller


class SaleController /*: Initializable*/ {
/*
    //var products:ObservableList<ProductEntity> = FXCollections.observableArrayList()

    var roleEntities = FXCollections.observableArrayList<Role>()


    private var employeeEntities:ObservableList<EmployeeEntity> = FXCollections.observableArrayList()



    //var productSelected:ProductEntity?=null


    @FXML private lateinit var roleVBox:VBox




    private lateinit var cubicCurve:CubicCurve




    @FXML
    private lateinit var descriptionText:Text

    @FXML
    private lateinit var codeProductTextField: TextField


    //////////////////////
    @FXML
    private lateinit var userTableView: TableView<EmployeeEntity>




    @FXML
    private lateinit var accountNumberTextField: TextField
    @FXML
    private lateinit var birthdayDatePicker: DatePicker
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
    private lateinit var telephoneTextField: TextField
    @FXML
    private lateinit var userNameTextField: TextField



    @FXML private lateinit var GenreCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var PassportCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var RolesCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var accountNumberCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var birthdayCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var directionCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var emailCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var firstNameCol: TableColumn<EmployeeEntity, String>
    //@FXML private lateinit var lastNameCol: TableColumn<Employee, String>
    //@FXML private lateinit var passwordCol: TableColumn<Employee, String>
    @FXML private lateinit var scheduleCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var telephoneCol: TableColumn<EmployeeEntity, String>
    @FXML private lateinit var userNameCol: TableColumn<EmployeeEntity, String>





    @FXML private lateinit var saveButton:Button
    @FXML private lateinit var deleteButton:Button
    @FXML private lateinit var updateButton:Button

    @FXML
    fun onUpdateButton(event: ActionEvent?) {
    }
    @FXML
    fun onDeleteButton(event: ActionEvent?) {
    }
    @FXML
    fun onSaveButton(event: ActionEvent?) {

         val rol = Role("Sale", mutableListOf(  Screen(ScreenEnum.SALE, mutableListOf( Action.ADD,Action.DELETE,Action.READ,Action.UPDATE ))  ) )
         val scheduleEntity = ScheduleEntity(null,1,1F,1.2F)


        /*val emp = EmployeeEntity(
            null,
            firstNameTextField.text,
            passportTextField.text,
            lastNameTextField.text,
            "male",
            directionField.text,
            emailTextField.text,
            telephoneTextField.text,
            GregorianCalendar.from( birthdayDatePicker.value.atStartOfDay( ZoneId.systemDefault() ) ),
            accountNumberTextField.text,
            userNameTextField.text,
            passwordField.text,
            listOf(rol) , listOf(scheduleEntity))

        employeeDto.save( emp )

        employeeEntities.add(
            emp
        )*/
    }

    ////////////////



    @FXML
    fun onAddProductButtonClick(event: ActionEvent) {
        /*if ( codeProductTextField.text.isNotBlank() || qtyTextField.text.isNotBlank() ){
            val product = ProductEntity()
            product.apply {
                id = 12
            }


            clearTextS()
        }*/
    }
    @FXML
    fun onUpdateProductButtonClick(event:ActionEvent){


    }
    @FXML
    fun onDeleteProductButtonClick(event:ActionEvent){
        /*products.remove(productSelected)
        clearTextS()*/
    }
    @FXML
    lateinit var cardCheckBox:RadioButton

    @FXML
    lateinit var payMethodRBGroup:ToggleGroup
    @FXML
    lateinit var choiceBoxRole: ChoiceBox<Role>
    @FXML
    lateinit var choiceBoxGender:ChoiceBox<String>
    @FXML
    lateinit var cashTextField:TextField
    @FXML
    lateinit var qtyTextField:TextField
    @FXML
    lateinit var changeTextField:TextField
    @FXML
    lateinit var countCol:TableColumn<EmployeeEntity,String>

    @FXML
    lateinit var layoutTabPaneController:VBox
    @FXML
    lateinit var tabPane:TabPane

    @FXML
    lateinit var hour:Text
    @FXML
    fun qtyOnKeyTyped(event: KeyEvent){}
    private val employeeDto = EmployeeDto()


    override fun initialize(location: URL?, resources: ResourceBundle?) {


        choiceBoxRole.items = roleEntities


        //employeeEntities.addAll( employeeDto.findAll(entity.EmployeeEntity::class.java) )












        tabPane.setStyle(
            "-fx-tab-max-height: -8;" +
                    "-fx-tab-header-area-visible: false;"
        );

        //tabPane.styleClass.add(hh)


        layoutTabPaneController.children.forEach{
            it.setOnMouseClicked (object :EventHandler<MouseEvent>{
                override fun handle(event: MouseEvent?) {
                    if (event != null) {
                        tabPane.selectionModel.select( layoutTabPaneController.children.indexOf( event.source ) )
                        println( "geni: "+layoutTabPaneController.children.indexOf( event.source ) )
                    }
                }

            })
        }



        val rolePane=RolePane(
            ScreenEnum.values().toList().map { it.name.lowercase().replaceFirstChar { it.uppercase() } },
            Action.values().toList().map { it.name.lowercase().replaceFirstChar { it.uppercase() } }
        )



        roleVBox.children.add( RoleTableView() )
        roleVBox.children.add( rolePane )

        cubicCurve = CubicCurve()
        cubicCurve.startX = 10.4
        cubicCurve.startY = 10.4
        cubicCurve.endX = 100.4
        cubicCurve.endY = 100.7

        createTimeLine( hour )






        firstNameCol.setCellValueFactory { data -> SimpleStringProperty("${data.value.firstName} ${data.value.lastName}") }
        //lastNameCol.setCellValueFactory { data -> SimpleStringProperty(data.value.lastName) }
        directionCol.setCellValueFactory { data -> SimpleStringProperty(data.value.direction) }

        directionCol.setCellValueFactory { data -> SimpleStringProperty(data.value.direction) }
        telephoneCol.setCellValueFactory { data -> SimpleStringProperty(data.value.telephone) }
        //passwordCol.setCellValueFactory { data -> SimpleStringProperty(data.value.password) }
        birthdayCol.setCellValueFactory { data -> SimpleStringProperty(data.value.birthDay.time.toString()) }
        GenreCol.setCellValueFactory { data -> SimpleStringProperty(data.value.genre) }
        emailCol.setCellValueFactory { data -> SimpleStringProperty(data.value.email) }
        accountNumberCol.setCellValueFactory { data -> SimpleStringProperty(data.value.bankAccount) }
        PassportCol.setCellValueFactory { data -> SimpleStringProperty(data.value.passport) }

        scheduleCol.setCellValueFactory { data -> SimpleStringProperty("data.value.schedule[0]".toString()) }
        RolesCol.setCellValueFactory { data -> SimpleStringProperty(data.value.role.name) }
        countCol.setCellValueFactory { data -> SimpleStringProperty((data.tableView.items.indexOf( data.value )+1).toString()) }
        userNameCol.setCellValueFactory { data -> SimpleStringProperty(data.value.userName) }




        userTableView.items = employeeEntities


        payMethodRBGroup.selectedToggleProperty().addListener(object:ChangeListener<Toggle>{
            override fun changed(observable: ObservableValue<out Toggle>?, oldValue: Toggle?, newValue: Toggle?) {
                if (observable != null ){
                    val a = newValue as RadioButton
                    if (a.id =="cardCheckBox"){
                        cashTextField.visibleProperty().bind(!a.selectedProperty())
                        changeTextField.visibleProperty().bind(!a.selectedProperty())
                    }else{
                        cashTextField.visibleProperty().bind(a.selectedProperty())
                        changeTextField.visibleProperty().bind(a.selectedProperty())
                    }
                }
            }
        })



    }

    private fun createTimeLine(text:Text){
        val timer = Timer()
        timer.scheduleAtFixedRate(object: TimerTask(){
            override fun run() {
                updateText(hour)
            }
        },0,100)
    }
    private fun updateText(text:Text){
        Platform.runLater {
            val currentTime = LocalDateTime.now()
            val formatTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            text.text = formatTime
        }
    }

 */
}

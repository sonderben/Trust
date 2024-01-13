package com.sonderben.trust.controller


import com.sonderben.trust.constant.Action
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.customView.RolePane
import com.sonderben.trust.db.HIbernateUtil
import com.sonderben.trust.db.ProductDto
import com.sonderben.trust.db.RoleDto
import com.sonderben.trust.model.*
import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.shape.CubicCurve
import javafx.scene.text.Text
import java.net.URL
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class SaleController : Initializable {

    var products:ObservableList<Product> = FXCollections.observableArrayList(
        /*Product("100","Chinola",10.0,0.11,16.2,1),
        Product("101","pan",45.0,0.11,51.2,2),
        Product("102","cafe",51.0,1.51,41.2,3)*/
    )

    var rol = Role("Sale", mutableListOf(  Screen(ScreenEnum.SALE, mutableListOf( Action.ADD,Action.DELETE,Action.READ,Action.UPDATE ))  ) )
    var schedule = Schedule(1,1.1f,1.1f)
    var persons:ObservableList<Person> = FXCollections.observableArrayList(
        Person("Jean","Jean-louis", Calendar.getInstance(),"passport","Male","Lascirie","b@gmail.com","4234","5454","fhh","bvcbvc",  listOf(rol) , listOf(schedule))
    )



    var productSelected:Product?=null
    @FXML
    private lateinit var codeCol: TableColumn<Product, String>

    @FXML private lateinit var roleVBox:VBox

    @FXML
    private lateinit var descriptionCol: TableColumn<Product, String>

    @FXML
    private lateinit var discountCol: TableColumn<Product, String>

    @FXML
    private lateinit var itbisCol: TableColumn<Product, String>


    private lateinit var cubicCurve:CubicCurve

    @FXML
    private lateinit var priceCol: TableColumn<Product, String>

    @FXML
    private lateinit var qtyCol: TableColumn<Product, String>

    @FXML
    private lateinit var totalCol:TableColumn<Product,String>

    @FXML
    private lateinit var tableView: TableView<Product>
    @FXML
    private lateinit var descriptionText:Text

    @FXML
    private lateinit var codeProductTextField: TextField


    //////////////////////
    @FXML
    private lateinit var userTableView: TableView<Person>




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



    @FXML private lateinit var GenreCol: TableColumn<Person, String>
    @FXML private lateinit var PassportCol: TableColumn<Person, String>
    @FXML private lateinit var RolesCol: TableColumn<Person, String>
    @FXML private lateinit var accountNumberCol: TableColumn<Person, String>
    @FXML private lateinit var birthdayCol: TableColumn<Person, String>
    @FXML private lateinit var directionCol: TableColumn<Person, String>
    @FXML private lateinit var emailCol: TableColumn<Person, String>
    @FXML private lateinit var firstNameCol: TableColumn<Person, String>
    @FXML private lateinit var lastNameCol: TableColumn<Person, String>
    //@FXML private lateinit var passwordCol: TableColumn<Person, String>
    @FXML private lateinit var scheduleCol: TableColumn<Person, String>
    @FXML private lateinit var telephoneCol: TableColumn<Person, String>
    @FXML private lateinit var userNameCol: TableColumn<Person, String>





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
        persons.add(
            Person(firstNameTextField.text,lastNameTextField.text,
                GregorianCalendar.from( birthdayDatePicker.value.atStartOfDay( ZoneId.systemDefault() ) ),passportTextField.text,
                "Male",directionField.text,
                emailTextField.text,accountNumberTextField.text,
                telephoneTextField.text,userNameTextField.text,
                passportTextField.text,  listOf(rol) , listOf(schedule))
        )
    }

    ////////////////



    @FXML
    fun onAddProductButtonClick(event: ActionEvent) {
        if ( codeProductTextField.text.isNotBlank() || qtyTextField.text.isNotBlank() ){
            val product = Product()
            product.apply {
                id = 12
            }

            products.add( Product(codeProductTextField.text,"cafe",51.0,15.1,41.2,qtyTextField.text.toInt()) )
            clearTextS()
        }
    }
    @FXML
    fun onUpdateProductButtonClick(event:ActionEvent){
        if (productSelected != null || products.isNotEmpty()){
            val temp = productSelected
            temp?.apply {
                quantity = ( qtyTextField.text.toInt() )
            }
            //products.remove(productSelected)
            val index = products.indexOf(productSelected)
            products[index] = temp
            clearTextS()
        }

    }
    @FXML
    fun onDeleteProductButtonClick(event:ActionEvent){
        products.remove(productSelected)
        clearTextS()
    }
    @FXML
    lateinit var cardCheckBox:RadioButton

    @FXML
    lateinit var payMethodRBGroup:ToggleGroup
    @FXML
    lateinit var cashTextField:TextField
    @FXML
    lateinit var qtyTextField:TextField
    @FXML
    lateinit var changeTextField:TextField
    @FXML
    lateinit var countCol:TableColumn<Person,String>

    @FXML
    lateinit var layoutTabPaneController:VBox
    @FXML
    lateinit var tabPane:TabPane

    @FXML
    lateinit var hour:Text
    @FXML
    fun qtyOnKeyTyped(event: KeyEvent){}


    override fun initialize(location: URL?, resources: ResourceBundle?) {








                var pr  = ProductDto()

                val a =Product()

                a.apply {
                    code = ("abc")

                     description= ("bla bla now")
                     price= (1.2)
                     discount= (0.2)
                     itbis= (12.2)
                     quantity = (12)

                }

                pr.save(a)


                products.addAll( pr.findAll() )



        layoutTabPaneController.children.forEach{
            it.setOnMouseClicked (object :EventHandler<MouseEvent>{
                override fun handle(event: MouseEvent?) {
                    if (event != null) {
                        tabPane.selectionModel.select( layoutTabPaneController.children.indexOf( event.source ) )
                    }
                }

            })
        }



        val rolePane=RolePane(
            ScreenEnum.values().toList().map { it.name.lowercase().replaceFirstChar { it.uppercase() } },
            Action.values().toList().map { it.name.lowercase().replaceFirstChar { it.uppercase() } }
        )



        roleVBox.children.add( rolePane )

        cubicCurve = CubicCurve()
        cubicCurve.startX = 10.4
        cubicCurve.startY = 10.4
        cubicCurve.endX = 100.4
        cubicCurve.endY = 100.7

        createTimeLine( hour )
        tableView.selectionModel.selectionMode = SelectionMode.SINGLE

        tableView.selectionModel.selectedItemProperty().addListener( object:ChangeListener<Product>{
            override fun changed(observable: ObservableValue<out Product>?, oldValue: Product?, newValue: Product?) {
                if (newValue != null){

                    productSelected = newValue
                    qtyTextField.text = newValue.quantity.toString()
                    codeProductTextField.text = newValue.code
                    descriptionText.text = "Desc: " + newValue.description.replaceFirstChar { it.toString() }
                }
            }
        })

        codeCol.setCellValueFactory{data->SimpleStringProperty( data.value.code )}
        itbisCol.setCellValueFactory{data->SimpleStringProperty( data.value.itbis.toString())}
        discountCol.setCellValueFactory{data->SimpleStringProperty( data.value.discount.toString() )}
        priceCol.setCellValueFactory{data->SimpleStringProperty( data.value.price.toString() )}
        qtyCol.setCellValueFactory{data->SimpleStringProperty( data.value.quantity.toString())}
        descriptionCol.setCellValueFactory{data->SimpleStringProperty( data.value.description.replaceFirstChar { it.uppercase() } )}
        totalCol.setCellValueFactory {
            SimpleStringProperty( it.value.total().toCurrency() )
        }

        tableView.items = products

        firstNameCol.setCellValueFactory { data -> SimpleStringProperty(data.value.firstName) }
        lastNameCol.setCellValueFactory { data -> SimpleStringProperty(data.value.lastName) }
        directionCol.setCellValueFactory { data -> SimpleStringProperty(data.value.direction) }

        directionCol.setCellValueFactory { data -> SimpleStringProperty(data.value.direction) }
        telephoneCol.setCellValueFactory { data -> SimpleStringProperty(data.value.telephone) }
        //passwordCol.setCellValueFactory { data -> SimpleStringProperty(data.value.password) }
        birthdayCol.setCellValueFactory { data -> SimpleStringProperty(data.value.birthDay.time.toString()) }
        GenreCol.setCellValueFactory { data -> SimpleStringProperty(data.value.genre) }
        emailCol.setCellValueFactory { data -> SimpleStringProperty(data.value.email) }
        accountNumberCol.setCellValueFactory { data -> SimpleStringProperty(data.value.bankAccount) }
        PassportCol.setCellValueFactory { data -> SimpleStringProperty(data.value.id) }

        scheduleCol.setCellValueFactory { data -> SimpleStringProperty(data.value.schedule[0].toString()) }
        RolesCol.setCellValueFactory { data -> SimpleStringProperty(data.value.roles.toString()) }
        countCol.setCellValueFactory { data -> SimpleStringProperty((data.tableView.items.indexOf( data.value )+1).toString()) }
        userNameCol.setCellValueFactory { data -> SimpleStringProperty(data.value.userName) }




        userTableView.items = persons







        products.addListener(object:ListChangeListener<Product>{
            override fun onChanged(c: ListChangeListener.Change<out Product>?) {

            }
        })

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
    private fun Double.toCurrency(): String {
        val local = Locale("en","us");
        val format = NumberFormat.getCurrencyInstance(local)

        return format.format(this)
    }
    private fun clearTextS(){
        qtyTextField.text = ""
        codeProductTextField.text = ""
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
}

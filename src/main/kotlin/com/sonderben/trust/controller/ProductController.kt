package com.sonderben.trust.controller

import com.sonderben.trust.*
import com.sonderben.trust.db.dao.CategoryDao
import com.sonderben.trust.db.dao.ProductDao
import com.sonderben.trust.qr_code.MessageListener
import com.sonderben.trust.qr_code.SocketMessageEvent
import com.sonderben.trust.viewUtil.ViewUtil
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.util.StringConverter
import java.net.URL
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class ProductController :Initializable,MessageListener,BaseController() {

    //private var socketMesageEvent = SocketMessageEvent(this)
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("init productController")

        editMenuItem()

       // socketMesageEvent.startingListening()

        employeeTf.text = Context.currentEmployee.value.userName

        categories =  CategoryDao.categories







        dateAddedCol.setCellValueFactory { data -> SimpleStringProperty( data.value.dateAdded.format() ) }
        codeCol.setCellValueFactory { data -> SimpleStringProperty(data.value.code) }
        descriptionCol.setCellValueFactory { data -> SimpleStringProperty( data.value.description ) }
        categoryCol.setCellValueFactory { data -> SimpleStringProperty( data.value.category.description ) }
        purchaseCol.setCellValueFactory { data -> SimpleStringProperty( data.value.purchasePrice.toString() ) }
        sellingCol.setCellValueFactory { data -> SimpleStringProperty( data.value.sellingPrice.toString() ) }
        exp_dateCole.setCellValueFactory { data -> SimpleStringProperty( data.value.expirationDate.format() ) }
        qtyCol.setCellValueFactory { data -> SimpleStringProperty( data.value.quantity.toString() ) }
        discountCol.setCellValueFactory { data -> SimpleStringProperty( data.value.discount.toString() ) }
        itbisCol.setCellValueFactory { data -> SimpleStringProperty( data.value.itbis.toString() ) }
        employeeCol.setCellValueFactory { data -> SimpleStringProperty( data.value.employee.userName ) }

        tableView.items = produtcs

        tableView.selectionModel.selectionMode = SelectionMode.SINGLE
        tableView.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            if (newValue != null) {
                var year = newValue.dateAdded.get(Calendar.YEAR)
                var month = newValue.dateAdded.get(Calendar.MONTH) + 1
                var day = newValue.dateAdded.get(Calendar.DAY_OF_MONTH)

                dateAddedDp.value = LocalDate.of(year, month, day)
                year = newValue.expirationDate.get(Calendar.YEAR)
                month = newValue.expirationDate.get(Calendar.MONTH) + 1
                day = newValue.expirationDate.get(Calendar.DAY_OF_MONTH)

                expDateDp.value = LocalDate.of(year, month, day)
                codeTf.text = newValue.code
                descriptionTf.text = newValue.description
                purchaseTf.text = newValue.purchasePrice.toString()
                sellingTf.text = newValue.sellingPrice.toString()

                qtyTf.text = newValue.quantity.toString()
                discountTf.text = newValue.discount.toString()
                itbisTf.text = newValue.quantity.toString()
                employeeTf.text = newValue.employee.userName

                categoryCb.selectionModel.select(categoryCb.items.indexOf(categoryCb.items.find { it.id == newValue.category.id }))
            }
        }

        categoryCb.converter = CategoryConverter()
        categoryCb.items = categories


    }
    @FXML
    private lateinit var categoryCb: ChoiceBox<CategoryEntity>

    @FXML
    private lateinit var categoryCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var codeCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var codeTf: TextField

    @FXML
    private lateinit var dateAddedCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var dateAddedDp: DatePicker

    @FXML
    private lateinit var descriptionCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var descriptionTf: TextField

    @FXML
    private lateinit var discountCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var discountTf: TextField

    @FXML
    private lateinit var employeeCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var employeeTf: TextField

    @FXML
    private lateinit var expDateDp: DatePicker

    @FXML
    private lateinit var exp_dateCole: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var itbisCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var itbisTf: TextField

    @FXML
    private lateinit var purchaseCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var purchaseTf: TextField

    @FXML
    private lateinit var qtyCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var qtyTf: TextField

    @FXML
    private lateinit var sellingCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var sellingTf: TextField
    @FXML
    private lateinit var bottomPanelVBOx:VBox

    @FXML
    private lateinit var tableView: TableView<ProductEntity>

    @FXML
    fun onDeleteBtn(event: ActionEvent) {
        println("onDeleteBtn")
    }

    @FXML
    fun onSaveBtn(event: ActionEvent) {

        val cc = categoryCb.selectionModel.selectedItem
        tempEmployee = Context.currentEmployee.value


       if (tempEmployee.id != null || tempEmployee.id != 0L ){
           val pro = ProductEntity(
               codeTf.text,
               descriptionTf.text,
               sellingTf.text.toDouble(),
               purchaseTf.text.toDouble(),
               discountTf.text.toDouble(),
               itbisTf.text.toDouble(),
               qtyTf.text.toInt(),
               qtyTf.text.toInt(),
               GregorianCalendar.getInstance(),
               GregorianCalendar.from( expDateDp.value.atStartOfDay( ZoneId.systemDefault() ) ),
               cc,
               tempEmployee
           )

           ProductDao.save(pro)

       }
    }

    @FXML
    fun goToCategoryOnMouseClicked(event: MouseEvent) {
        val cc = CategoryDialog()

        cc.initOwner(HelloApplication.primary)
        cc.showAndWait()

    }

    @FXML
    fun onUpdateBtn(event: ActionEvent) {
        println("onUpdateBtn")
    }

    fun hideBottomPanelOnMouseClicked(){
         bottomPanelVBOx.hide()
    }
    private  var produtcs:ObservableList<ProductEntity> = ProductDao.products//FXCollections.observableArrayList( ProductDao.products )
    private lateinit var tempEmployee:EmployeeEntity
    private lateinit var categories:ObservableList<CategoryEntity>


    class CategoryConverter: StringConverter<CategoryEntity>() {
        override fun toString(`object`: CategoryEntity?): String {
            return `object`?.description ?: ""
        }

        override fun fromString(string: String?): CategoryEntity {
            return CategoryEntity()
        }
    }

    override fun onReceiveMessage(data: String) {

        codeTf.text = data
    }

    override fun onDestroy() {
       // socketMesageEvent.removeListener()
        println("clear productController")
    }

    private fun editMenuItem() {
        MainController.editMenu.items.clear()


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

        }


        MainController.editMenu.items.addAll(  saveMenuItem,updateMenuItems,deleteMenuItems  )

    }
}
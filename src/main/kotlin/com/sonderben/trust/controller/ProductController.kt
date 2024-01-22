package com.sonderben.trust.controller

import com.sonderben.trust.Util
import dto.CategoryDto
import dto.EmployeeDto
import dto.ProductDto
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.util.StringConverter
import java.net.URL
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class ProductController :Initializable {
    override fun initialize(location: URL?, resources: ResourceBundle?) {



        //tempEmployee = employeeDto.findAll()[0]
        produtcs =  FXCollections.observableArrayList( productDto.findAll() )
        categories = FXCollections.observableArrayList( categoryDto.findAll() )

        dateAddedCol.setCellValueFactory { data -> SimpleStringProperty( Util.formatDate( data.value.dateAdded.time ) ) }
        codeCol.setCellValueFactory { data -> SimpleStringProperty(data.value.code) }
        descriptionCol.setCellValueFactory { data -> SimpleStringProperty( data.value.description ) }
        categoryCol.setCellValueFactory { data -> SimpleStringProperty( data.value.category.description ) }
        purchaseCol.setCellValueFactory { data -> SimpleStringProperty( data.value.purchasePrice.toString() ) }
        sellingCol.setCellValueFactory { data -> SimpleStringProperty( data.value.sellingPrice.toString() ) }
        exp_dateCole.setCellValueFactory { data -> SimpleStringProperty( Util.formatDate( data.value.expirationDate.time ) ) }
        qtyCol.setCellValueFactory { data -> SimpleStringProperty( data.value.quantity.toString() ) }
        discountCol.setCellValueFactory { data -> SimpleStringProperty( data.value.discount.toString() ) }
        itbisCol.setCellValueFactory { data -> SimpleStringProperty( data.value.itbis.toString() ) }
        employeeCol.setCellValueFactory { data -> SimpleStringProperty( data.value.employee.fullName ) }

        tableView.items = produtcs

        tableView.selectionModel.selectionMode = SelectionMode.SINGLE
        tableView.selectionModel.selectedItemProperty().addListener(object :ChangeListener<ProductEntity>{
            override fun changed(
                observable: ObservableValue<out ProductEntity>?,
                oldValue: ProductEntity?,
                newValue: ProductEntity?
            ) {
                if (newValue != null) {
                    var year = newValue.dateAdded.get( Calendar.YEAR )
                    var month = newValue.dateAdded.get( Calendar.MONTH ) + 1
                    var day = newValue.dateAdded.get( Calendar.DAY_OF_MONTH )

                    dateAddedDp.value = LocalDate.of(year,month,day)
                     year = newValue.expirationDate.get( Calendar.YEAR )
                     month = newValue.expirationDate.get( Calendar.MONTH ) + 1
                     day = newValue.expirationDate.get( Calendar.DAY_OF_MONTH )

                    expDateDp.value = LocalDate.of( year,month,day  )
                    codeTf.text = newValue.code
                    descriptionTf.text = newValue.description
                    purchaseTf.text = newValue.purchasePrice.toString()
                    sellingTf.text = newValue.sellingPrice.toString()

                    qtyTf.text = newValue.quantity.toString()
                    discountTf.text = newValue.discount.toString()
                    itbisTf.text = newValue.quantity.toString()
                    employeeTf.text = newValue.employee.fullName
                }
            }
        })

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
    private lateinit var tableView: TableView<ProductEntity>

    @FXML
    fun onDeleteBtn(event: ActionEvent) {
        println("onDeleteBtn")
    }

    @FXML
    fun onSaveBtn(event: ActionEvent) {
        val dto = ProductDto()
        val cc = categoryCb.selectionModel.selectedItem

        val pro = ProductEntity(
            codeTf.text,
            descriptionTf.text,
            sellingTf.text.toDouble(),
            purchaseTf.text.toDouble(),
            discountTf.text.toDouble(),
            itbisTf.text.toDouble(),
            qtyTf.text.toInt(),
            GregorianCalendar.getInstance(),
            GregorianCalendar.from( expDateDp.value.atStartOfDay( ZoneId.systemDefault() ) ),
            cc,
            tempEmployee
        )

        produtcs.add( dto.save( pro ) )
    }

    @FXML
    fun goToCategoryOnMouseClicked(event: MouseEvent) {
        val cc = CategoryDialog()
        cc.showAndWait()
    }

    @FXML
    fun onUpdateBtn(event: ActionEvent) {
        println("onUpdateBtn")
    }
    private lateinit var produtcs:ObservableList<ProductEntity>
    private lateinit var tempEmployee:EmployeeEntity
    private lateinit var categories:ObservableList<CategoryEntity>
    private  var productDto = ProductDto()
    private  var categoryDto = CategoryDto()
    private var employeeDto = EmployeeDto()

    class CategoryConverter: StringConverter<CategoryEntity>() {
        override fun toString(`object`: CategoryEntity?): String {
            return `object`?.description ?: ""
        }

        override fun fromString(string: String?): CategoryEntity {
            return CategoryEntity()
        }
    }
}
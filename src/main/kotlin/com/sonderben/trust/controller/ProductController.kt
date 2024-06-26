package com.sonderben.trust.controller

import com.sonderben.trust.*
import com.sonderben.trust.db.service.CategoryService
import com.sonderben.trust.db.service.ProductService
import com.sonderben.trust.qr_code.MessageListener
import com.sonderben.trust.viewUtil.ViewUtil
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.VBox
import javafx.util.StringConverter
import java.lang.Exception
import java.net.URL
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class ProductController :Initializable,MessageListener,BaseController() {
    lateinit var searcTextField: TextField

    //private var socketMesageEvent = SocketMessageEvent(this)
    private var currentProductSelected:ProductEntity?=null
    private val loading = ViewUtil.loadingView()
    private lateinit var resources: ResourceBundle
    override fun initialize(location: URL?, resourcesBundle: ResourceBundle) {
        resources = resourcesBundle
        disableQueryControlButton()
        editMenuItem()
        MainController.hideBottomBar(false) { hideBottomPanelOnMouseClicked() }


        sellbyCb.items.addAll(resources.getString("unit") , resources.getString("weight")  )


        discountTf.onlyFloat()
        itbisTf.onlyFloat()
        qtyTf.onlyFloat()


        searcTextField.textProperty().addListener { observableValue, s, s2 ->
            if (s2.isBlank()){
                tableView.items=ProductService.getInstance().entities
            }
        }

       // socketMesageEvent.startingListening()

        employeeTf.text = Context.currentEmployee.value.userName

        categories =  CategoryService.getInstance().entities



        dateAddedCol.setCellValueFactory { data -> SimpleStringProperty( data.value.dateAdded.format() ) }
        codeCol.setCellValueFactory { data -> SimpleStringProperty(data.value.code.padStart(12,'0')) }
        descriptionCol.setCellValueFactory { data -> SimpleStringProperty( data.value.description ) }
        categoryCol.setCellValueFactory { data -> SimpleStringProperty( data.value.category.description ) }
        purchaseCol.setCellValueFactory { data -> SimpleStringProperty( data.value.purchasePrice.toString() ) }
        sellingCol.setCellValueFactory { data -> SimpleStringProperty( data.value.sellingPrice.toString() ) }
        exp_dateCole.setCellValueFactory { data -> SimpleStringProperty( data.value.expirationDate.format() ) }
        qtyCol.setCellValueFactory { data -> SimpleStringProperty( data.value.quantity.toString() ) }
        discountCol.setCellValueFactory { data -> SimpleStringProperty( data.value.discount.toString() ) }
        itbisCol.setCellValueFactory { data -> SimpleStringProperty( data.value.itbis.toString() ) }
        employeeCol.setCellValueFactory { data -> SimpleStringProperty( data.value.employee.userName ) }
        howSaleCol.setCellValueFactory { data -> SimpleStringProperty(resources?.getString( data.value.sellBy.lowercase() )
            ?: data.value.sellBy) }

            tableView.items = produtcs

        tableView.selectionModel.selectionMode = SelectionMode.SINGLE
        tableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                currentProductSelected = newValue
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
                itbisTf.text = newValue.itbis.toString()
                employeeTf.text = newValue.employee.userName

                categoryCb.selectionModel.select(newValue.category)
                sellbyCb.selectionModel.select( if( newValue.sellBy.equals("unit",true)) 0 else 1 )
            }
        }

        categoryCb.converter = CategoryConverter()
            categoryCb.items = categories


        tableView.selectionModel.selectedIndices.addListener( ListChangeListener { change->
            if ( change.list.isEmpty() ){
                enableActionButton(mainPane,true)
            }
            else{
                enableActionButton(mainPane,false)
            }
        } )

    }

















    @FXML private lateinit var categoryCb: ChoiceBox<CategoryEntity>

    @FXML private lateinit var categoryCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var codeCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var codeTf: TextField

    @FXML private lateinit var dateAddedCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var dateAddedDp: DatePicker

    @FXML private lateinit var descriptionCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var descriptionTf: TextField

    @FXML private lateinit var discountCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var discountTf: TextField

    @FXML private lateinit var employeeCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var employeeTf: TextField

    @FXML private lateinit var expDateDp: DatePicker

    @FXML private lateinit var exp_dateCole: TableColumn<ProductEntity, String>

    @FXML private lateinit var itbisCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var itbisTf: TextField

    @FXML private lateinit var purchaseCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var purchaseTf: TextField

    @FXML private lateinit var qtyCol: TableColumn<ProductEntity, String>

    @FXML private lateinit var qtyTf: TextField

    @FXML
    private lateinit var sellingCol: TableColumn<ProductEntity, String>
    @FXML lateinit var mainPane: VBox
    @FXML lateinit var howSaleCol: TableColumn<ProductEntity, String>
    @FXML lateinit var sellbyCb: ChoiceBox<String>
    @FXML private lateinit var sellingTf: TextField
    @FXML private lateinit var bottomPanelVBOx:VBox

    @FXML private lateinit var tableView: TableView<ProductEntity>

    @FXML fun onDeleteBtn() {

        if(currentProductSelected!=null) {
            loading.show()
            ProductService.getInstance().delete(currentProductSelected!!.id).subscribe({
                clear(mainPane)
                loading.close()
            },{
                th-> println( th.message )
                 loading.close()
            })
        }else{
            ViewUtil.customAlert(ViewUtil.WARNING,resources.getString("please_select_product"))
            .show()
        }


    }

    @FXML fun onSaveBtn() {

        val cc = categoryCb.selectionModel.selectedItem
        val tempEmployee = Context.currentEmployee.value


       if ( validateProduct(tempEmployee,cc) ){
           //val kk = if(sellbyCb.selectionModel.selectedIndex==0) "1" else "gen"
           val pro = ProductEntity(
               codeTf.text,
               if(sellbyCb.selectionModel.selectedIndex==0) "unit" else "weight",
               descriptionTf.text,
               sellingTf.text.toDouble(),
               purchaseTf.text.toDouble(),
               discountTf.text.toDouble(),
               itbisTf.text.toDouble(),
               qtyTf.text.toFloat(),
               qtyTf.text.toFloat(),
               GregorianCalendar.getInstance(),
               GregorianCalendar.from( expDateDp.value.atStartOfDay( ZoneId.systemDefault() ) ),
               cc,
               tempEmployee
           )

           ProductService.getInstance().save( pro ).subscribe({ clear(mainPane) },{ th-> println(th.message) } )

       }else{
           println("not validate")
       }
    }

    private fun validateProduct(employee: EmployeeEntity?, category: CategoryEntity?): Boolean {

        if (employee == null){
            ViewUtil.customAlert(ViewUtil.ERROR,resources.getString("cant_find_current_employee")) {
                val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
                HelloApplication.primary.scene = Scene(fxmlLoader.load(), 720.0, 440.0)
            }
                .show()

            return false
        }
        if (sellbyCb.selectionModel.selectedIndex == -1){
            ViewUtil.customAlert(ViewUtil.WARNING,resources.getString("please_choose_how_product_sold")).show()
            return false
        }
        if (category == null){
            ViewUtil.customAlert(ViewUtil.WARNING,resources.getString("please_select_category")).show()
            return false
        }
        if (codeTf.text.isEmpty() || descriptionTf.text.isEmpty()){
            ViewUtil.customAlert(ViewUtil.WARNING,resources.getString("fill_all_text_fields")).show()
            return false
        }

        try {
            GregorianCalendar.from( expDateDp.value.atStartOfDay( ZoneId.systemDefault() ) )
            if (sellingTf.text.toDouble()<0 || purchaseTf.text.toDouble()<0 || purchaseTf.text.toDouble()<0 || discountTf.text.toDouble()<0 || itbisTf.text.toDouble()<0 || qtyTf.text.toFloat() < 0){
                ViewUtil.customAlert(ViewUtil.WARNING,resources.getString("fill_all_text_fields")).show()
                return false
            }
            //  if(sellbyCb.selectionModel.selectedIndex==0) "unit" else "weight",
            if(sellbyCb.selectionModel.selectedIndex==0 && !qtyTf.text.toFloat().isInt()){
                ViewUtil.customAlert(ViewUtil.WARNING,resources.getString("if_product_sold_per_unit")).show()
                return false
            }
        }
        catch (e:Exception){
            ViewUtil.customAlert(ViewUtil.WARNING,resources.getString("fill_all_text_fields_corect_info")).show()
            return false
        }
        return true
    }

    @FXML fun onClearBtn() {
        clear(mainPane)
    }



    @FXML fun goToCategoryOnMouseClicked() {
        val cc = CategoryDialog()

        cc.initOwner(HelloApplication.primary)
        cc.showAndWait()

    }

    @FXML fun onUpdateBtn() {



        if(currentProductSelected!=null) {
            if (validateProduct(Context.currentEmployee.value, categoryCb.selectionModel.selectedItem )){
                currentProductSelected!!.apply {
                    code = codeTf.text
                    description = descriptionTf.text
                    sellingPrice = sellingTf.text.toDouble()
                    purchasePrice = purchaseTf.text.toDouble()
                    discount = discountTf.text.toDouble()
                    itbis = itbisTf.text.toDouble()
                    quantity = qtyTf.text.toFloat()
                    quantityRemaining = qtyTf.text.toFloat()
                    category = categoryCb.selectionModel.selectedItem
                    sellBy = if(sellbyCb.selectionModel.selectedIndex==0) "unit" else "weight"


                }
                ProductService.getInstance().update(currentProductSelected!!) .subscribe({
                    clear(mainPane)
                },{th-> println(th.message) })
            }
        }else{
            ViewUtil.customAlert(ViewUtil.WARNING,resources.getString("please_select_product"))
                .show()
        }
    }

    fun hideBottomPanelOnMouseClicked(){
         bottomPanelVBOx.changeVisibility()
    }
    private  var produtcs:ObservableList<ProductEntity> = ProductService.getInstance().entities
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
        MainController.editMenu?.items?.clear()


        val saveMenuItem =  MenuItem(resources.getString("save"))
        saveMenuItem.setOnAction {
            onSaveBtn()
        }
        val updateMenuItems =  MenuItem(resources.getString("update"))
        updateMenuItems.setOnAction {
            onUpdateBtn()
        }

        val deleteMenuItems =  MenuItem(resources.getString("delete"))
        deleteMenuItems.setOnAction {
            onDeleteBtn()
        }
        val clearMenuItems =  MenuItem(resources.getString("clear"))
        clearMenuItems.setOnAction {
            clear(mainPane)
        }




        MainController.editMenu?.items?.addAll(  saveMenuItem,updateMenuItems,deleteMenuItems,clearMenuItems  )

    }

    fun searchBtnAction() {
        tableView.items = ProductService.getInstance().entities.filtered { it.description.startsWith(searcTextField.textTrim(),ignoreCase = true) ||it.code.startsWith(searcTextField.textTrim(),true) }
    }

    fun searchTextFieldOnKeyRelased(keyEvent: KeyEvent) {

        if (keyEvent.code.equals( KeyCode.ENTER ))
            searchBtnAction()
    }
}
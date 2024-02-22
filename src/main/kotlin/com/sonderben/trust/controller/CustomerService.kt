package com.sonderben.trust.controller

import com.sonderben.trust.*
import com.sonderben.trust.db.dao.CustomerDao
import com.sonderben.trust.db.dao.InvoiceDao
import com.sonderben.trust.viewUtil.ViewUtil
import entity.CustomerEntity
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.util.Callback
import java.net.URL
import java.util.*
import kotlin.collections.List

class CustomerService:Initializable,BaseController() {
    private  var customers = CustomerDao.customers
    private var selectedCustomer:CustomerEntity?=null
    private var customerToChangePoint:CustomerEntity?=null
    override fun initialize(p0: URL?, p1: ResourceBundle?) {


        rTableview.selectionModel.selectionMode = SelectionMode.MULTIPLE

        rTableview.rowFactory = Callback<TableView<InvoiceDao.ProductReturned>,TableRow<InvoiceDao.ProductReturned>>{tablevieww ->
            object :TableRow<InvoiceDao.ProductReturned>(){
                override fun updateItem(p0: InvoiceDao.ProductReturned?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    if (p0!=null && p0.isReturned){
                        isDisable = true
                        style = "-fx-background-color:#FF000018;"
                    }
                }
            }
        }


        cBirthdayCol.setCellValueFactory { data -> SimpleStringProperty(data.value.birthDay.format()) }
        cCodeCol.setCellValueFactory { d -> SimpleStringProperty(d.value.code) }
        cTelCol.setCellValueFactory { d -> SimpleStringProperty(d.value.telephone) }
        cEmailCol.setCellValueFactory { d -> SimpleStringProperty(d.value.email) }
        cDirCol.setCellValueFactory { d -> SimpleStringProperty(d.value.direction) }
        cGenreCol.setCellValueFactory { d -> SimpleStringProperty(d.value.genre) }
        cPointCol.setCellValueFactory { d -> SimpleStringProperty( d.value.point.toString() ) }

        cFullNameCol.setCellValueFactory { d -> SimpleStringProperty(d.value.firstName + " "+d.value.lastName) }
        cPassportCol.setCellValueFactory { d -> SimpleStringProperty(d.value.passport) }

        cTableview.selectionModel.selectedItemProperty().addListener(object :ChangeListener<CustomerEntity>{
            override fun changed(p0: ObservableValue<out CustomerEntity>?, p1: CustomerEntity?, newValue: CustomerEntity?) {
                if(newValue!=null){
                    selectedCustomer = newValue
                    cFirstnameTf.text = newValue.firstName
                    cLastNameTf.text = newValue.lastName
                    cGenderCb.selectionModel.select( newValue.genre.replaceFirstChar { it.uppercase() } )
                    cBirthdayDp.value = newValue.birthDay.toLocalDate()
                    cpCodeTf.text = newValue.code
                    cTelTf.text = newValue.telephone
                    cEmailTf.text = newValue.email
                    cDirectionTtf.text  = newValue.direction
                   // cPointTf.text = newValue.point.toString()
                    cPassportTf.text = newValue.passport

                }
            }
        })


        cTableview.items = customers

    }

    @FXML
    private lateinit var cTableview:TableView<CustomerEntity>

    @FXML
    private lateinit var rTableview:TableView<InvoiceDao.ProductReturned>

    @FXML
    private lateinit var cBirthdayCol: TableColumn<CustomerEntity, String>

    @FXML
    private lateinit var cBirthdayDp: DatePicker

    @FXML
    private lateinit var cCodeCol: TableColumn<CustomerEntity, String>

    @FXML
    private lateinit var cDirCol: TableColumn<CustomerEntity, String>

    @FXML
    private lateinit var cDirectionTtf: TextField

    @FXML
    private lateinit var cEmailCol: TableColumn<CustomerEntity, String>

    @FXML
    private lateinit var cEmailTf: TextField

    @FXML
    private lateinit var cFirstnameTf: TextField

    @FXML
    private lateinit var cFullNameCol: TableColumn<CustomerEntity, String>

    @FXML
    private lateinit var cGenderCb: ChoiceBox<String>

    @FXML
    private lateinit var cGenreCol: TableColumn<CustomerEntity, String>

    @FXML
    private lateinit var cLastNameTf: TextField

    @FXML
    private lateinit var cPassportCol: TableColumn<CustomerEntity, String>

    @FXML
    private lateinit var cPassportTf: TextField

    @FXML
    private lateinit var cPointCol: TableColumn<CustomerEntity, String>

    /*@FXML
    private lateinit var cPointTf: TextField*/

    @FXML
    private lateinit var cTelCol: TableColumn<CustomerEntity, String>

    @FXML
    private lateinit var cTelTf: TextField

    @FXML
    private lateinit var changePointOnSave: Button

    @FXML
    private lateinit var cpCodeTf: TextField

    @FXML
    private lateinit var cpFirstnameTf: TextField

    @FXML
    private lateinit var cpLastnameTf: TextField

    @FXML
    private lateinit var cpOnSave: Button

    @FXML
    private lateinit var cpPassportTf: TextField

    @FXML
    private lateinit var cpPointTf: TextField

    @FXML
    private lateinit var cpChangeTfConversion: TextField
    @FXML
    private lateinit var cpPointTfConversion: TextField

    @FXML
    private lateinit var rCategoryCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var rCodeCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var rDateExpiredCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var rDescriptionCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var rInvoiceTf: TextField

    @FXML
    private lateinit var rQuantityCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var rReasonTf: TextField

    @FXML
    private lateinit var toggleGroup:ToggleGroup

    @FXML
    private lateinit var rTotal: TableColumn<InvoiceDao.ProductReturned, String>
    var productsReturned: List<InvoiceDao.ProductReturned> = mutableListOf()

    @FXML
    fun cpOnsearch(event: ActionEvent) {
        customerToChangePoint = CustomerDao.findByCode( cpCodeTf.textTrim() )
        if (customerToChangePoint != null){
            cpFirstnameTf.text = customerToChangePoint!!.firstName
            cpLastnameTf.text = customerToChangePoint!!.lastName
            cpPassportTf.text = customerToChangePoint!!.passport
            cpPointTf.text = customerToChangePoint!!.point.toString()
            cpPointTfConversion.text = cpPointTf.text
            cpChangeTfConversion.text = (customerToChangePoint!!.point/100).toString()
        }else{
            println("no encontrado usuarion con codigo: ${cpCodeTf.textTrim()}")
        }
    }

    @FXML
    fun onClearCustomer(event: ActionEvent) {
        selectedCustomer?.id=null
        selectedCustomer = null
    }

    @FXML
    fun onDeleteCustomer(event: ActionEvent) {


        selectedCustomer?.let { CustomerDao.delete( it.id )
            cTableview.selectionModel.select(null)
        }




    }

    @FXML
    fun onSaveCustomer(event: ActionEvent) {
        val cal = Calendar.getInstance()
        val customer = CustomerEntity()

        customer.apply {
            firstName = cFirstnameTf.textTrim()
            lastName = cLastNameTf.textTrim()
            genre = cGenderCb.selectionModel.selectedItem
            birthDay = cBirthdayDp.value.toCalendar()
            code = cpCodeTf.textTrim()
            telephone = cTelTf.textTrim()
            email = cEmailTf.textTrim()
            direction = cDirectionTtf.textTrim()
            passport = cPassportTf.textTrim()
            code =  "${cal.get(Calendar.MONTH)+1}${cal.get(Calendar.YEAR)}${cTelTf.textTrim()}"
            point = 0
            CustomerDao.save(this)
        }




    }

    @FXML
    fun onUpdateCustomer(event: ActionEvent) {
        selectedCustomer?.let {


            val customer = CustomerEntity()

            customer.apply {
                id = selectedCustomer!!.id
                firstName = cFirstnameTf.textTrim()
                lastName = cLastNameTf.textTrim()
                genre = cGenderCb.selectionModel.selectedItem
                birthDay = cBirthdayDp.value.toCalendar()
                code = cpCodeTf.textTrim()
                telephone = cTelTf.textTrim()
                email = cEmailTf.textTrim()
                direction = cDirectionTtf.textTrim()
                passport = cPassportTf.textTrim()
                point= selectedCustomer!!.point
                CustomerDao.update(this)
            }



           println("li update ? "+ CustomerDao.update( customer ))
            cTableview.selectionModel.clearSelection()
        }
    }

    @FXML
    fun rOnClear(event: ActionEvent) {

    }

    @FXML
    fun rOnSave(event: ActionEvent) {
        val itemSelected = rTableview.selectionModel.selectedItems

        if (itemSelected.isNotEmpty()){
            val action = if( ( toggleGroup.selectedToggle as RadioButton).id.equals("changeRbtn") ) "CHANGE" else "MONEY"
            val idProductsSold  = itemSelected.map { it.productSoldId }
            val isSave = InvoiceDao.saveProductReturned(productsReturned[0].invoiceId,idProductsSold,Context.currentEmployee.value.id,rReasonTf.textTrim(),action)
            if (isSave){
                ViewUtil.createAlert(Alert.AlertType.INFORMATION,"Saved","Product return with success").showAndWait()
            }else{
                ViewUtil.createAlert(Alert.AlertType.WARNING,"Error","Product don't return.").showAndWait()
            }
        }else{
            ViewUtil.createAlert(Alert.AlertType.WARNING,"NO item selected","You must select some items first").showAndWait()
        }
    }

    @FXML
    fun rOnSearch(event: ActionEvent) {
         productsReturned =InvoiceDao.findByInvoiceCode(rInvoiceTf.textTrim())

        if (productsReturned.isNotEmpty()){

            rTotal.setCellValueFactory { d->SimpleStringProperty( d.value.totalPrice.toString() ) }
            rCodeCol.setCellValueFactory { d->SimpleStringProperty( d.value.code ) }
            rCategoryCol.setCellValueFactory { d->SimpleStringProperty( d.value.category ) }
            rDescriptionCol.setCellValueFactory { d->SimpleStringProperty( d.value.description ) }
            rDateExpiredCol.setCellValueFactory { d->SimpleStringProperty( d.value.dateCreated.format() ) }
            rQuantityCol.setCellValueFactory { d->SimpleStringProperty( d.value.quantity.toString() ) }
            /*rTotal.setCellValueFactory { d->SimpleStringProperty() }
            rTotal.setCellValueFactory { d->SimpleStringProperty() }
            rTotal.setCellValueFactory { d->SimpleStringProperty() }*/


            rTableview.items = FXCollections.observableArrayList( productsReturned )
        }
    }

    override fun onDestroy() {

    }
}
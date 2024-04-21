package com.sonderben.trust.controller

import com.sonderben.trust.*
import com.sonderben.trust.db.service.CustomerService
import com.sonderben.trust.db.dao.InvoiceDao
import com.sonderben.trust.db.service.InvoiceService
import com.sonderben.trust.viewUtil.ViewUtil
import entity.CustomerEntity
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.util.Callback
import java.net.URL
import java.util.*
import kotlin.collections.List

class CustomerService : Initializable, BaseController() {
    lateinit var tabPane: TabPane
    lateinit var changePointTab: Tab
    lateinit var returnProductTab: Tab
    lateinit var customerTab: Tab

    lateinit var changePointMainPane: VBox
    lateinit var returnProductMainPane: VBox
    lateinit var customerMainPane: VBox


    private var customers = CustomerService.getInstance().entities
    private var selectedCustomer: CustomerEntity? = null
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        disableQueryControlButton()
        editMenuItem()

        rTableview.selectionModel.selectionMode = SelectionMode.MULTIPLE


        rTableview.rowFactory =
            Callback<TableView<InvoiceDao.ProductToReturned>, TableRow<InvoiceDao.ProductToReturned>> {
                object : TableRow<InvoiceDao.ProductToReturned>() {
                    override fun updateItem(p0: InvoiceDao.ProductToReturned?, p1: Boolean) {
                        super.updateItem(p0, p1)
                        if (p0 != null && p0.isReturned) {
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
        cPointCol.setCellValueFactory { d -> SimpleStringProperty(d.value.point.toString()) }

        cFullNameCol.setCellValueFactory { d -> SimpleStringProperty(d.value.firstName + " " + d.value.lastName) }
        cPassportCol.setCellValueFactory { d -> SimpleStringProperty(d.value.passport) }

        cTableview.selectionModel.selectedItemProperty().addListener(object : ChangeListener<CustomerEntity> {
            override fun changed(
                p0: ObservableValue<out CustomerEntity>?,
                p1: CustomerEntity?,
                newValue: CustomerEntity?
            ) {
                if (newValue != null) {
                    selectedCustomer = newValue
                    cFirstnameTf.text = newValue.firstName
                    cLastNameTf.text = newValue.lastName
                    cGenderCb.selectionModel.select(newValue.genre.replaceFirstChar { it.uppercase() })
                    cBirthdayDp.value = newValue.birthDay.toLocalDate()
                    cpCodeTf.text = newValue.code
                    cTelTf.text = newValue.telephone
                    cEmailTf.text = newValue.email
                    cDirectionTtf.text = newValue.direction
                    // cPointTf.text = newValue.point.toString()
                    cPassportTf.text = newValue.passport

                }
            }
        })


        cTableview.items = customers

    }

    private fun editMenuItem() {
        MainController.editMenu?.items?.clear()


        val saveMenuItem = MenuItem("Save")
        saveMenuItem.setOnAction {

        }
        val updateMenuItems = MenuItem("Update")
        updateMenuItems.setOnAction {

        }

        val deleteMenuItems = MenuItem("Delete")
        deleteMenuItems.setOnAction {

        }
        val clearMenuItems = MenuItem("Clear")
        clearMenuItems.setOnAction {
            clear()
        }


        MainController.editMenu?.items?.addAll(saveMenuItem, updateMenuItems, deleteMenuItems, clearMenuItems)

        cTableview.selectionModel.selectedIndices.addListener(ListChangeListener { change ->
            if (change.list.isEmpty()) {
                enableActionButton(customerMainPane, true)
            } else {
                enableActionButton(customerMainPane, false)
            }
        })

    }

    @FXML
    private lateinit var cTableview: TableView<CustomerEntity>

    @FXML
    private lateinit var rTableview: TableView<InvoiceDao.ProductToReturned>

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
    private lateinit var cpPassportTf: TextField

    @FXML
    private lateinit var cpPointTf: TextField

    @FXML
    private lateinit var cpChangeTfConversion: TextField

    @FXML
    private lateinit var cpPointTfConversion: TextField

    @FXML
    private lateinit var rCategoryCol: TableColumn<InvoiceDao.ProductToReturned, String>

    @FXML
    private lateinit var rCodeCol: TableColumn<InvoiceDao.ProductToReturned, String>

    @FXML
    private lateinit var rDateExpiredCol: TableColumn<InvoiceDao.ProductToReturned, String>

    @FXML
    private lateinit var rDescriptionCol: TableColumn<InvoiceDao.ProductToReturned, String>

    @FXML
    private lateinit var rInvoiceTf: TextField

    @FXML
    private lateinit var rQuantityCol: TableColumn<InvoiceDao.ProductToReturned, String>

    @FXML
    private lateinit var rReasonTf: TextField

    @FXML
    private lateinit var toggleGroup: ToggleGroup

    @FXML
    private lateinit var rTotal: TableColumn<InvoiceDao.ProductToReturned, String>
    var productsReturned: List<InvoiceDao.ProductToReturned> = mutableListOf()

    @FXML
    fun cpOnsearch() {
        /*customerToChangePoint =*/ CustomerService.getInstance().findByCode(cpCodeTf.textTrim())
            .subscribe({customersFound->
                cpFirstnameTf.text = customersFound!!.firstName
                cpLastnameTf.text = customersFound!!.lastName
                cpPassportTf.text = customersFound!!.passport
                cpPointTf.text = customersFound!!.point.toString()
                cpPointTfConversion.text = cpPointTf.text
                cpChangeTfConversion.text = (customersFound!!.point / 100).toString()
            },{println("no encontrado usuarion con codigo: ${cpCodeTf.textTrim()}")})

    }

    @FXML
    fun onClearCustomer() {
        clear()

    }

    @FXML
    fun onDeleteCustomer() {

        selectedCustomer?.let {
            CustomerService.getInstance().delete(it.id)
                .subscribe({ clear() }, { th -> println(th.message) })
        }

    }

    @FXML
    fun onSaveCustomer() {
        val lastCountCustomer = Context.readJson()["last_count_customer"] as Long

        if (validateCustomer()) {
            val cal = Calendar.getInstance()
            val customer = CustomerEntity()
            customer.apply {
                firstName = cFirstnameTf.textTrim()
                lastName = cLastNameTf.textTrim()
                genre = cGenderCb.selectionModel.selectedItem
                birthDay = cBirthdayDp.value.toCalendar()
                telephone = cTelTf.textTrim()
                email = cEmailTf.textTrim()
                direction = cDirectionTtf.textTrim()
                passport = cPassportTf.textTrim()
                val month = cal.get( Calendar.MONTH ) + 1
                val last2DigitYear  =cal.get( Calendar.YEAR).toString().substring(2,4)

                code = "${ month }${ last2DigitYear }${ lastCountCustomer + 1 }".padStart(12,'0')


                point = 0

            }
            CustomerService.getInstance().save(customer)
                .subscribe({
                    clear(customerMainPane)
                    Context.writeJson("last_count_customer", lastCountCustomer + 1)
                }, { th -> println(th.message) })
        }


    }

    private fun validateCustomer(): Boolean {
        if (cFirstnameTf.textTrim().isBlank() || cLastNameTf.textTrim().isBlank() ||
            cTelTf.textTrim().isBlank() || cEmailTf.textTrim().isBlank() ||
            cDirectionTtf.textTrim().isBlank() || cPassportTf.textTrim().isBlank()
        ) {
            ViewUtil.customAlert(ViewUtil.WARNING, "please make sure you fill out all the text fields.").show()
            return false
        }

        if (cGenderCb.selectionModel.selectedItem == null) {
            ViewUtil.customAlert(ViewUtil.WARNING,  "Please select a gender").show()
            return false
        }
        if (cBirthdayDp.value == null) {
            ViewUtil.customAlert(ViewUtil.WARNING,  "Please select a birthday").show()
            return false
        }


        return true
    }

    @FXML
    fun onUpdateCustomer() {
        if (selectedCustomer != null) {
            if (validateCustomer()) {
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
                        point = selectedCustomer!!.point

                    }
                    CustomerService.getInstance().update(customer)
                        .subscribe({ clear() }, { th -> println(th.message) })


                }
            }
        } else {
            ViewUtil.customAlert(ViewUtil.WARNING, "Please select a customer first.").showAndWait()

        }
    }

    @FXML
    fun rOnClear() {
        clear()
    }

    @FXML
    fun rOnSave() {
        val itemSelected = rTableview.selectionModel.selectedItems

        if (itemSelected.isNotEmpty()) {
            val action = if ((toggleGroup.selectedToggle as RadioButton).id.equals("changeRbtn")) "CHANGE" else "MONEY"
            val idProductsSold = itemSelected.map { it.productSoldId }
            /*val isSave =*/ InvoiceService.getInstance().saveProductReturned(
                productsReturned[0].invoiceId,
                idProductsSold,
                Context.currentEmployee.value.id,
                rReasonTf.textTrim(),
                action
            ).subscribe({
                ViewUtil.customAlert(ViewUtil.WARNING,  "Product return with success").showAndWait()

            },{
                ViewUtil.customAlert(ViewUtil.WARNING, "Product don't return.").showAndWait()

            })

        } else {
            ViewUtil.customAlert(ViewUtil.WARNING, "You must select some items first")
                .showAndWait()
        }
    }

    @FXML
    fun rOnSearch() {
        /*productsReturned =*/ InvoiceService.getInstance().findByInvoiceCode(rInvoiceTf.textTrim())
            .subscribe({
                rTotal.setCellValueFactory { d -> SimpleStringProperty(d.value.totalPrice.toString()) }
                rCodeCol.setCellValueFactory { d -> SimpleStringProperty(d.value.code) }
                rCategoryCol.setCellValueFactory { d -> SimpleStringProperty(d.value.category) }
                rDescriptionCol.setCellValueFactory { d -> SimpleStringProperty(d.value.description) }
                rDateExpiredCol.setCellValueFactory { d -> SimpleStringProperty(d.value.dateCreated.format()) }
                rQuantityCol.setCellValueFactory { d -> SimpleStringProperty(d.value.quantity.toString()) }
                /*rTotal.setCellValueFactory { d->SimpleStringProperty() }
                rTotal.setCellValueFactory { d->SimpleStringProperty() }
                rTotal.setCellValueFactory { d->SimpleStringProperty() }*/


                rTableview.items = FXCollections.observableArrayList(productsReturned)
            },{})


    }

    override fun onDestroy() {

    }

    fun clear() {
        when (tabPane.selectionModel.selectedItem) {
            customerTab -> {
                clear(customerMainPane)
                selectedCustomer?.id = null
                selectedCustomer = null
            }

            changePointTab -> clear(changePointMainPane)
            returnProductTab -> clear(returnProductMainPane)
            else -> throw Exception("bad pane")
        }
    }

    fun cpOnClear() {
        clear()
    }
}
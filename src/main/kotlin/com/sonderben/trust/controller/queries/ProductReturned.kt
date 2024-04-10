package com.sonderben.trust.controller.queries

import com.sonderben.trust.controller.BaseController
import com.sonderben.trust.db.dao.InvoiceDao
import com.sonderben.trust.db.service.InvoiceService
import com.sonderben.trust.format
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import java.net.URL
import java.util.*

class ProductReturned:Initializable, BaseController() {
   // private lateinit var productsReturned: ObservableList<InvoiceDao.ProductReturned>
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        //productsReturned =

        invoiceCol.setCellValueFactory { d->SimpleStringProperty( d.value.invoiceCodeBar ) }
        codCol.setCellValueFactory { d->SimpleStringProperty( d.value.productCode ) }
        desciptionCol.setCellValueFactory { d->SimpleStringProperty(d.value.description) }
        dateExpiredCol.setCellValueFactory { d->SimpleStringProperty( d.value.expirationDate.format() ) }
        quantityCol.setCellValueFactory { d->SimpleStringProperty( d.value.quantity.toString() ) }
        totalCol.setCellValueFactory { d->SimpleStringProperty (d.value.totalPrice.toString() ) }
        //customerCodeCol.setCellValueFactory { d->SimpleStringProperty( d.value.customerCode ) }
        employeeFullNameCol.setCellValueFactory { d->SimpleStringProperty( d.value.employee ) }
        dateReturnecol.setCellValueFactory { d->SimpleStringProperty( d.value.dateReturned.format() ) }
        reasonCol.setCellValueFactory { d->SimpleStringProperty(d.value.reson) }
        actionCol.setCellValueFactory { d->SimpleStringProperty(d.value.action) }
        dateBoughtCol.setCellValueFactory {d->SimpleStringProperty(d.value.dateBought.format())}

       InvoiceService.getInstance().getProductReturned()
           .subscribe({
               tableView.items = FXCollections.observableArrayList( it )
           },{})


    }

    override fun onDestroy() {

    }

    @FXML
    private lateinit var invoiceCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var codCol: TableColumn<InvoiceDao.ProductReturned, String>

   // @FXML
   // private lateinit var customerCodeCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var dateExpiredCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var reasonCol:TableColumn<InvoiceDao.ProductReturned,String>
    @FXML
    private lateinit var actionCol:TableColumn<InvoiceDao.ProductReturned,String>
    @FXML
    private lateinit var dateBoughtCol:TableColumn<InvoiceDao.ProductReturned,String>

    @FXML
    private lateinit var dateReturnecol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var desciptionCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var employeeFullNameCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var quantityCol: TableColumn<InvoiceDao.ProductReturned, String>

    @FXML
    private lateinit var tableView: TableView<InvoiceDao.ProductReturned>

    @FXML
    private lateinit var totalCol: TableColumn<InvoiceDao.ProductReturned, String>
}
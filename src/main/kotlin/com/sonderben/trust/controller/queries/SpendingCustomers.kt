package com.sonderben.trust.controller.queries

import com.sonderben.trust.db.dao.CustomerDao
import com.sonderben.trust.db.service.CustomerService
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import java.net.URL
import java.util.*

class SpendingCustomers:Initializable {
    //private val spendingOrFrequentCustomer = CustomerDao.spendingOrFrequentCustomer()
    override fun initialize(p0: URL?, p1: ResourceBundle?) {

        customerCol.setCellValueFactory { d->SimpleStringProperty(d.value.cusomerCode) }
        pointCol.setCellValueFactory { d->SimpleStringProperty( d.value.point.toString() ) }
        totalSpendCol.setCellValueFactory { d->SimpleStringProperty( d.value.totalSpend.toString() ) }
        totalProductCol.setCellValueFactory { d->SimpleStringProperty( d.value.totalProductBought.toString() ) }

        CustomerService.getInstance().spendingOrFrequentCustomer()
            .subscribe({
                tableView.items = FXCollections.observableArrayList( it )
            },{})

    }

    @FXML
    private lateinit var tableView: TableView<CustomerDao.SpendingOrFrequentCustomer>

    @FXML
    private lateinit var customerCol: TableColumn<CustomerDao.SpendingOrFrequentCustomer, String>

    @FXML
    private lateinit var pointCol: TableColumn<CustomerDao.SpendingOrFrequentCustomer, String>

    @FXML
    private lateinit var totalProductCol: TableColumn<CustomerDao.SpendingOrFrequentCustomer, String>

    @FXML
    private lateinit var totalSpendCol: TableColumn<CustomerDao.SpendingOrFrequentCustomer, String>
}
package com.sonderben.trust.controller.queries

import com.sonderben.trust.controller.BaseController
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import java.net.URL
import java.util.*

class ProductReturned:Initializable, BaseController() {
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        categoryCol.setCellValueFactory { d->SimpleStringProperty() }
        codCol.setCellValueFactory { d->SimpleStringProperty() }
        desciptionCol.setCellValueFactory { d->SimpleStringProperty() }
        dateExpiredCol.setCellValueFactory { d->SimpleStringProperty() }
        quantityCol.setCellValueFactory { d->SimpleStringProperty() }
        totalCol.setCellValueFactory { d->SimpleStringProperty() }
        customerCodeCol.setCellValueFactory { d->SimpleStringProperty() }
        employeeFullNameCol.setCellValueFactory { d->SimpleStringProperty() }
        dateReturnecol.setCellValueFactory { d->SimpleStringProperty() }

    }

    override fun onDestroy() {

    }

    @FXML
    private lateinit var categoryCol: TableColumn<Any, String>

    @FXML
    private lateinit var codCol: TableColumn<Any, String>

    @FXML
    private lateinit var customerCodeCol: TableColumn<Any, String>

    @FXML
    private lateinit var dateExpiredCol: TableColumn<Any, String>

    @FXML
    private lateinit var dateReturnecol: TableColumn<Any, String>

    @FXML
    private lateinit var desciptionCol: TableColumn<Any, String>

    @FXML
    private lateinit var employeeFullNameCol: TableColumn<Any, String>

    @FXML
    private lateinit var quantityCol: TableColumn<Any, String>

    @FXML
    private lateinit var tableView: TableView<Any>

    @FXML
    private lateinit var totalCol: TableColumn<Any, String>
}
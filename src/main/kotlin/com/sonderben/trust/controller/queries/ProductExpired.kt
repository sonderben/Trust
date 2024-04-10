package com.sonderben.trust.controller.queries

import com.sonderben.trust.controller.BaseController
import com.sonderben.trust.controller.ProductDetails
import com.sonderben.trust.db.service.ProductDetailService
import com.sonderben.trust.format
import entity.ProductEntity
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import java.net.URL
import java.util.*

class ProductExpired:Initializable, BaseController() {
    override fun initialize(p0: URL?, p1: ResourceBundle?) {

        codeCol.setCellValueFactory { data-> SimpleStringProperty(data.value.code) }
        descriptionCol.setCellValueFactory { data-> SimpleStringProperty(data.value.description) }
        categoryCol.setCellValueFactory { data-> SimpleStringProperty(data.value.category.description) }
        qtyEnteredCol.setCellValueFactory { data-> SimpleStringProperty(data.value.quantity.toString()) }
        qtyRemainingCol.setCellValueFactory { data-> SimpleStringProperty(data.value.quantityRemaining.toString()) }
        expirationCol.setCellValueFactory { data -> SimpleStringProperty( data.value.expirationDate.format()) }

        ProductDetailService.getInstance().findProductsExpired()
            .subscribe({
                tableView.items.addAll( it )
            },{})



    }

    @FXML
    private lateinit var codeCol: TableColumn<ProductEntity, String>
    @FXML
    private lateinit var descriptionCol: TableColumn<ProductEntity, String>
    @FXML
    private lateinit var categoryCol: TableColumn<ProductEntity, String>
    @FXML
    private lateinit var qtyEnteredCol: TableColumn<ProductEntity, String>
    @FXML
    private lateinit var qtyRemainingCol: TableColumn<ProductEntity, String>
    @FXML
    private lateinit var tableView: TableView<ProductEntity>
    @FXML
    lateinit var expirationCol: TableColumn<ProductEntity, String>
    override fun onDestroy() {

    }
}
package com.sonderben.trust.controller

import dto.ProductDto
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
import javafx.scene.text.Text
import java.net.URL
import java.text.NumberFormat
import java.util.*

class Sale :Initializable{
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        tableView.selectionModel.selectionMode = SelectionMode.SINGLE
        tableView.selectionModel.selectedItemProperty().addListener( object: ChangeListener<ProductEntity> {
            override fun changed(observable: ObservableValue<out ProductEntity>?, oldValue: ProductEntity?, newValue: ProductEntity?) {
                if (newValue != null){

                    mProductSelected = newValue
                    qtyTextField.text = newValue.quantity.toString()
                    codeProductTextField.text = newValue.code
                    descriptionText.text = "Desc: " + newValue.description.replaceFirstChar { it.toString() }
                }
            }
        })


        codeCol.setCellValueFactory{data-> SimpleStringProperty( data.value.code ) }
        itbisCol.setCellValueFactory{data-> SimpleStringProperty( data.value.itbis.toString()) }
        discountCol.setCellValueFactory{data-> SimpleStringProperty( data.value.discount.toString() ) }
        priceCol.setCellValueFactory{data-> SimpleStringProperty( data.value.sellingPrice.toString() ) }
        qtyCol.setCellValueFactory{data-> SimpleStringProperty( data.value.quantity.toString()) }
        descriptionCol.setCellValueFactory{data-> SimpleStringProperty( data.value.description.replaceFirstChar { it.uppercase() } ) }
        totalCol.setCellValueFactory {
            SimpleStringProperty( it.value.total().toCurrency() )
        }

        tableView.items = mProducts

    }
    @FXML
    private lateinit var cardCheckBox: RadioButton

    @FXML
    private lateinit var cashTextField: TextField

    @FXML
    private lateinit var changeTextField: TextField

    @FXML
    private lateinit var codeCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var codeProductTextField: TextField

    @FXML
    private lateinit var descriptionCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var descriptionText: Text

    @FXML
    private lateinit var discountCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var hour: Text

    @FXML
    private lateinit var itbisCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var payMethodRBGroup: ToggleGroup

    @FXML
    private lateinit var priceCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var qtyCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var qtyTextField: TextField

    @FXML
    private lateinit var tableView: TableView<ProductEntity>

    @FXML
    private lateinit var totalCol: TableColumn<ProductEntity, String>

    @FXML
    fun onAddProductButtonClick(event: ActionEvent) {
        if ( codeProductTextField.text.isNotBlank() || qtyTextField.text.isNotBlank() ){
            val product = mProductDto.findByCode(codeProductTextField.text)
            val qty = qtyTextField.text.toIntOrNull()
            if ( qty != null /*&& codeProductTextField.text.length >=4*/){
                product.quantity = qty
                mProducts.add( product )
                clearTextS()
            }else{
                val alert = Alert(Alert.AlertType.WARNING)
                alert.title = "Warning"
                alert.headerText = "Quantity can't be empty"
                alert.contentText = "Please enter a valid quantity"
                alert.showAndWait()

            }
        }
    }

    @FXML
    fun onDeleteProductButtonClick(event: ActionEvent) {
        mProducts.remove(mProductSelected)
        clearTextS()
    }

    @FXML
    fun onUpdateProductButtonClick(event: ActionEvent) {
        if (mProductSelected != null || mProducts.isNotEmpty()){
            val temp = mProductSelected
            temp?.apply {
                quantity = ( qtyTextField.text.toInt() )
            }
            val index = mProducts.indexOf(mProductSelected)
            mProducts[index] = temp
            clearTextS()
        }
    }

    private fun clearTextS(){
        qtyTextField.text = ""
        codeProductTextField.text = ""
    }
    private fun Double.toCurrency(): String {
        val local = Locale("en","us");
        val format = NumberFormat.getCurrencyInstance(local)

        return format.format(this)
    }
    var mProducts: ObservableList<ProductEntity> = FXCollections.observableArrayList()
    var mProductSelected:ProductEntity?=null
    val mProductDto = ProductDto()

}
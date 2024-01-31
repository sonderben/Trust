package com.sonderben.trust.controller

import com.sonderben.trust.Context
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.db.dao.InvoiceDao
import com.sonderben.trust.db.dao.ProductDao
import com.sonderben.trust.qr_code.MessageListener
import com.sonderben.trust.qr_code.SocketMessageEvent
import entity.InvoiceEntity
import entity.ProductEntity
import entity.ProductSaled
import javafx.application.Platform
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
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.text.Text
import java.net.URL
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random
import kotlin.random.nextLong

class Sale :Initializable,MessageListener{
    private var socketMesageEvent = SocketMessageEvent(this)

    companion object{

    }
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        //socketMesageEvent.startingListening()


        qtyTotal.prefWidthProperty().bind( qtyCol.widthProperty() )
        itbisTotal.prefWidthProperty().bind( qtyCol.widthProperty() )
        discountTotal.prefWidthProperty().bind( qtyCol.widthProperty() )
        grandTotal.prefWidthProperty().bind( qtyCol.widthProperty() )


        tableView.selectionModel.selectionMode = SelectionMode.SINGLE
        tableView.selectionModel.selectedItemProperty().addListener( object: ChangeListener<ProductEntity> {
            override fun changed(observable: ObservableValue<out ProductEntity>?, oldValue: ProductEntity?, newValue: ProductEntity?) {
                if (newValue != null){

                    mProductSelected = newValue
                    qtyTextField.text = newValue.quantity.toString()
                    codeProductTextField.text = newValue.code
                   // descriptionText.text = "Desc: " + newValue.description.replaceFirstChar { it.toString() }
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

        mProducts.addListener(object :ListChangeListener<ProductEntity>{
            override fun onChanged(c: ListChangeListener.Change<out ProductEntity>?) {
                if (c !=null){
                    var totalItbis = 0.0
                    var totalQty = 0.0
                    var bigTotal = 0.0
                    var totaldiscount =0.0
                    for (prod in mProducts){
                        totalItbis += prod.itbis
                        totalQty += prod.quantity
                        totaldiscount += prod.discount
                        bigTotal += prod.total()
                    }
                    itbisTotal.text = (totalItbis * totalQty).toString()
                    qtyTotal.text = totalQty.toString()
                    grandTotal.text = bigTotal.toString()
                    discountTotal.text = (totaldiscount*totalQty).toString()
                }
            }
        })

        createTimeLine( hour )


        mainNode.setOnKeyTyped { event ->
            if (event.code==KeyCode.ENTER){
                if (qtyTextField.isFocused){
                    cashTextField.requestFocus()
                }else if (cashTextField.isFocused){
                    codeProductTextField.requestFocus()
                }else if (codeProductTextField.isFocused){
                    qtyTextField.requestFocus()
                }else{
                    codeProductTextField.requestFocus()
                }
            }
        }

    }
    /*@FXML
    private lateinit var cardCheckBox: RadioButton*/

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
/*
    @FXML
    private lateinit var descriptionText: Text*/
    @FXML
    private lateinit var mainNode: VBox

    @FXML
    private lateinit var discountCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var hour: Text

    @FXML
    private lateinit var itbisCol: TableColumn<ProductEntity, String>

    /*@FXML
    private lateinit var payMethodRBGroup: ToggleGroup*/

    @FXML
    private lateinit var priceCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var qtyCol: TableColumn<ProductEntity, String>

    //

    @FXML
    private lateinit var  discountTotal: TextField
    @FXML
    private lateinit var qtyTotal: TextField
    @FXML
    private lateinit var  itbisTotal: TextField
    @FXML
    private lateinit var grandTotal: TextField


     //

    @FXML
    private lateinit var qtyTextField: TextField

    @FXML
    private lateinit var tableView: TableView<ProductEntity>

    @FXML
    private lateinit var totalCol: TableColumn<ProductEntity, String>

    @FXML
    fun onAddProductButtonClick(event: ActionEvent) {
        beep()
        if ( codeProductTextField.text.isNotBlank()  ){

            findProductBy( codeProductTextField.text )
        }
    }

    @FXML
    fun onDeleteProductButtonClick(event: ActionEvent) {
        mProducts.remove(mProductSelected)
        clearTextS()
    }

    @FXML
    fun codeOnKeyReleased(event: KeyEvent) {
        if (event.code.equals(KeyCode.ENTER)){
            mProducts.add( ProductDao.findProductByCode( codeProductTextField.text ) )
            println("KeyCode.ENTER")
        }

    }
    @FXML
    fun onPay(event: ActionEvent) {
       if(mProducts.size>0){
           val ra = Random.nextLong(LongRange(10_000_000,99_999_999))
           InvoiceDao.save(
               InvoiceEntity(mProducts.map { ProductSaled(it,false) },Context.currentEmployee.value,null,ra.toString(), Calendar.getInstance())
           )
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
    var mProducts: ObservableList<ProductEntity> = FXCollections.observableArrayList(  )
    var mProductSelected:ProductEntity?=null

    private fun createTimeLine(text:Text){
        val timer = Timer()
        timer.scheduleAtFixedRate(object: TimerTask(){
            override fun run() {
                updateText(hour)
            }
        },0,100)
    }
    private fun updateText(text:Text){
        Platform.runLater {
            val currentTime = LocalDateTime.now()
            val formatTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            text.text = formatTime
        }
    }
    val mMedia = Media(HelloApplication.javaClass.getResource("audio/beep7.mp3").toString())
    var mMediaplayer = MediaPlayer(mMedia)

    private fun beep(){

        mMediaplayer.stop()
        mMediaplayer.play()
    }

    override fun onReceiveMessage(data: String) {
        Platform.runLater(Runnable {
            findProductBy(code = data)





        })
    }

    private fun findProductBy(code:String){
        val productFind = mProducts.find { it.code == code }
        if (qtyTextField.text.isBlank()){
            qtyTextField.text = "1"
        }
        if (productFind!=null){
            //change the quantity
            val index = mProducts.indexOf( productFind )
            productFind.quantity += qtyTextField.text.toInt()
            mProducts[index] = productFind
            clearTextS()
            beep()
        }else{
            val product = ProductDao.findProductByCode( codeProductTextField.text )
            if (product !=null){
                product.quantity = qtyTextField.text.toInt()
                mProducts.add( product )
                clearTextS()
                beep()
            }else{
                val alert = Alert(Alert.AlertType.WARNING)
                alert.title = "Product not found:"
                alert.headerText = "code prod. : $code"
                alert.showAndWait()
            }
        }
    }

}
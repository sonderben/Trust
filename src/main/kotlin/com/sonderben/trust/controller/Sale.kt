package com.sonderben.trust.controller

import com.sonderben.trust.Context
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.db.dao.CustomerDao
import com.sonderben.trust.db.dao.InvoiceDao
import com.sonderben.trust.db.dao.ProductDao
import com.sonderben.trust.hide
import com.sonderben.trust.qr_code.MessageListener
import com.sonderben.trust.qr_code.SocketMessageEvent
import com.sonderben.trust.viewUtil.ViewUtil
import entity.CustomerEntity
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
import javafx.scene.input.InputMethodEvent
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
    private var mCurrentCustomer:CustomerEntity? = null


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        //socketMesageEvent.startingListening()


        qtyTotal.prefWidthProperty().bind( qtyCol.widthProperty() )
        grandTotal.prefWidthProperty().bind( qtyCol.widthProperty() )


        tableView.setOnKeyReleased { event ->
            val tableViewSelectedItems: ObservableList<ProductEntity> = tableView.selectionModel.selectedItems
            if (event.code==KeyCode.DELETE && tableViewSelectedItems.size>0){
                mProducts.removeAll( tableViewSelectedItems )
                clearAll()
            }
        }
        tableView.selectionModel.selectionMode = SelectionMode.MULTIPLE
        tableView.selectionModel.selectedItemProperty().addListener( object: ChangeListener<ProductEntity> {
            override fun changed(observable: ObservableValue<out ProductEntity>?, oldValue: ProductEntity?, newValue: ProductEntity?) {
                if (newValue != null){

                    if (! bottomPanelVBOx.isVisible ){
                        bottomPanelVBOx.hide()
                    }

                    mProductSelected = newValue
                    qtyTextField.text = newValue.quantity.toString()
                    codeProductTextField.text = newValue.code
                }
            }
        })
        tableView.placeholder = Text("")









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

                    qtyTotal.text = totalQty.toString()
                    grandTotal.text = bigTotal.toString()

                }
            }
        })

        qtyTextField.setOnKeyReleased { event ->
            if(event.code == KeyCode.ENTER){
                findProductBy( codeProductTextField.text )
                codeProductTextField.requestFocus()
            }
        }

        createTimeLine( hour )




    }

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
    private lateinit var payBtn:Button
    @FXML
    private lateinit var mainNode: VBox

    @FXML
    private lateinit var discountCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var hour: Text

    @FXML
    private lateinit var itbisCol: TableColumn<ProductEntity, String>



    @FXML
    private lateinit var priceCol: TableColumn<ProductEntity, String>

    @FXML
    private lateinit var qtyCol: TableColumn<ProductEntity, String>

    //


    @FXML
    private lateinit var qtyTotal: TextField

    @FXML
    private lateinit var grandTotal: TextField


     //

    @FXML
    private lateinit var qtyTextField: TextField

    @FXML
    private lateinit var tableView: TableView<ProductEntity>

    @FXML
    private lateinit var totalCol: TableColumn<ProductEntity, String>

    @FXML lateinit var customerCode:TextField



    @FXML
    fun onDeleteProductButtonClick(event: ActionEvent) {
        mProducts.remove(mProductSelected)
        clearTextS()
    }

    @FXML
    fun codeOnKeyReleased(event: KeyEvent) {
        if (event.code.equals(KeyCode.ENTER)){
            if ( codeProductTextField.text.isNotBlank()  ){
                qtyTextField.requestFocus()
            }else if (mProducts.size!=0){
                cashTextField.requestFocus()
            }
        }
    }


    @FXML
    fun onKeyTypedCash(event: KeyEvent) {
        changeTextField.text = (cashTextField.text.toDouble()-grandTotal.text.toDouble()).toString()
        if (/*event.code == KeyCode.ENTER &&*/ changeTextField.text.toDouble()>-1){
            payBtn.requestFocus()
        }
    }
    @FXML
    fun onPay(event: ActionEvent) {
       if(mProducts.size>0){
           if (cashTextField.text.isBlank()){
               cashTextField.text = "0.0"
           }
           if (cashTextField.text.toDouble()>=grandTotal.text.toDouble()){
               val codeBar = Random.nextLong(LongRange(10_000_000,99_999_999))
              val isPayed = InvoiceDao.save(
                   InvoiceEntity(mProducts.map { ProductSaled(it,false) },Context.currentEmployee.value,mCurrentCustomer,codeBar.toString(), Calendar.getInstance())
               )
               if (isPayed){
                   mCurrentCustomer?.let { CustomerDao.updatePoint(it.id,
                       (changeTextField.text.toDouble()/100.0).toLong()
                   ) }
                   println("point: ${(cashTextField.text.toDouble()).toLong()}")
                   println("point: ${(cashTextField.text.toDouble()/100.0).toLong()}")
                   ViewUtil.createAlert(Alert.AlertType.CONFIRMATION,"Payment","Pay with success").showAndWait()
                   clearAll()
                   customerCode.requestFocus()
               }

           }else{
               ViewUtil.createAlert(Alert.AlertType.WARNING,"Invalid Data","Cash must be greater than grand total").showAndWait()

           }
       }else{
           ViewUtil.createAlert(Alert.AlertType.WARNING,"Invalid Data","There is no product to pay").showAndWait()
       }

    }

    private fun clearAll(){
        clearTextS()
        cashTextField.text = ""
        changeTextField.text = ""
        grandTotal.text = ""
        qtyTotal.text = ""
        customerCode.text = ""
        mProducts.clear()
    }

    @FXML
    fun onKeyReleasedCustomer(event: KeyEvent) {
       // println("genial: "+event.code.name)
        if (event.code.equals( KeyCode.ENTER )){

            var code = (event.source as TextField).text
            code = code.padStart(12,'0')
            println(code)
            mCurrentCustomer = CustomerDao.findByCode(code)
            mCurrentCustomer.let {
                (event.source as TextField).text = mCurrentCustomer!!.fullName
            }

            codeProductTextField.requestFocus()

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
    @FXML
    private lateinit var bottomPanelVBOx:VBox
    override fun onReceiveMessage(data: String) {
        Platform.runLater(Runnable {
            findProductBy(code = data)
        })
    }

    private fun findProductBy(code:String){
        val tempCode = code.padStart(12,'0')
        val productFind = mProducts.find { it.code == tempCode }
        if (qtyTextField.text.isBlank()){
            qtyTextField.text = "1"
        }
        if (productFind!=null){
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
                ViewUtil.createAlert(
                    Alert.AlertType.WARNING,
                    "Product not found",
                    "code prod. : $tempCode"
                ).showAndWait()
            }
        }
    }

    fun hideBottomPanelOnMouseClicked(){
        bottomPanelVBOx.hide()
    }

}
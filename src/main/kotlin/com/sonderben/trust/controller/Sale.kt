package com.sonderben.trust.controller

import com.sonderben.trust.*
import com.sonderben.trust.db.dao.CustomerDao
import com.sonderben.trust.db.dao.InvoiceDao
import com.sonderben.trust.qr_code.MessageListener
import com.sonderben.trust.viewUtil.ViewUtil
import entity.CustomerEntity
import entity.InvoiceEntity
import entity.ProductSaled
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.text.Text
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random
import kotlin.random.nextLong

class Sale :Initializable,MessageListener,BaseController(){
    //private var socketMesageEvent = SocketMessageEvent(this)
    private var mCurrentCustomer:CustomerEntity? = null

    override fun onDestroy(){

       /* if (socketMesageEvent.isListening()){
            socketMesageEvent.removeListener()
            println("clear Sale")
        }*/
        println("clear saleController")


    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        //socketMesageEvent.startingListening()

        editMenuItem()
        MainController.hideBottomBar(true)

        //customerCode.onlyInt()
        codeProductTextField.onlyInt()
        qtyTextField//.onlyInt()
        cashTextField.onlyInt()


        qtyTotal.prefWidthProperty().bind( qtyCol.widthProperty() )
        grandTotal.prefWidthProperty().bind( qtyCol.widthProperty() )


        tableView.setOnKeyReleased { event ->
            val tableViewSelectedItems: ObservableList<ProductSaled> = tableView.selectionModel.selectedItems
            if (event.code==KeyCode.DELETE && tableViewSelectedItems.size>0){
                mProducts.removeAll( tableViewSelectedItems )
                clearAll()
            }
        }
        tableView.selectionModel.selectionMode = SelectionMode.MULTIPLE
        tableView.selectionModel.selectedItemProperty().addListener( object: ChangeListener<ProductSaled> {
            override fun changed(observable: ObservableValue<out ProductSaled>?, oldValue: ProductSaled?, newValue: ProductSaled?) {
                if (newValue != null){

                    if (! bottomPanelVBOx.isVisible ){
                        bottomPanelVBOx.changeVisibility()
                    }

                    mProductSaledSelected = newValue
                    qtyTextField.text = newValue.qty.toString()
                    codeProductTextField.text = newValue.code
                }
            }
        })
        tableView.placeholder = Text("")



        codeCol.setCellValueFactory{data-> SimpleStringProperty( data.value.code ) }
        itbisCol.setCellValueFactory{data-> SimpleStringProperty( data.value.itbis.toString()) }
        discountCol.setCellValueFactory{data-> SimpleStringProperty( data.value.discount.toString() ) }
        priceCol.setCellValueFactory{data-> SimpleStringProperty( data.value.price.toString() ) }

        qtyCol.setCellValueFactory{data-> SimpleStringProperty( if (data.value.sellBy.equals("unit",true)) data.value.qty.toString() else "${data.value.qty} Lb") }

        descriptionCol.setCellValueFactory{data-> SimpleStringProperty( data.value.description.replaceFirstChar { it.uppercase() } ) }
        totalCol.setCellValueFactory {
            SimpleStringProperty( it.value.total.toCurrency() )
        }

        tableView.items = mProducts

        mProducts.addListener(ListChangeListener<ProductSaled> { c ->
            if (c !=null){
                var totalItbis = 0.0
                var totalQty = 0.0
                var bigTotal = 0.0
                var totaldiscount =0.0
                for (prod in mProducts){
                    totalItbis += prod.itbis
                    totalQty += prod.qty
                    totaldiscount += prod.discount
                    bigTotal += prod.total
                }

                qtyTotal.text = totalQty.toString()
                grandTotal.text = bigTotal.toString()

            }
        })

        qtyTextField.setOnKeyReleased { event ->
            if(event.code == KeyCode.ENTER){
                findProductBy( codeProductTextField.text )
                codeProductTextField.requestFocus()
            }
        }

        createTimeLine()




    }

    private fun editMenuItem() {
        MainController.editMenu?.items?.clear()


        val saveMenuItem =  MenuItem("Pay")
        saveMenuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN )

        saveMenuItem.setOnAction {
            pay()
        }
        val clearMenuItems =  MenuItem("Clear")
        clearMenuItems.accelerator = KeyCodeCombination(KeyCode.DIGIT2,KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN)

        clearMenuItems.setOnAction {
            clearTextS()
        }

        val cancelMenuItems =  MenuItem("Cancel")
        cancelMenuItems.accelerator = KeyCodeCombination(KeyCode.DIGIT3,KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN)

        clearMenuItems.setOnAction {
            clearAll()
        }


        MainController.editMenu?.items?.addAll(  saveMenuItem,/*updateMenuItem,deleteMenuItem,*/clearMenuItems,cancelMenuItems  )

    }

    @FXML
    private lateinit var cashTextField: TextField

    @FXML
    private lateinit var changeTextField: TextField

    @FXML
    private lateinit var codeCol: TableColumn<ProductSaled, String>

    @FXML
    private lateinit var codeProductTextField: TextField

    @FXML
    private lateinit var descriptionCol: TableColumn<ProductSaled, String>

    @FXML
    private lateinit var payBtn:Button
    @FXML
    private lateinit var mainNode: VBox

    @FXML
    private lateinit var discountCol: TableColumn<ProductSaled, String>

    @FXML
    private lateinit var hour: Text

    @FXML
    private lateinit var itbisCol: TableColumn<ProductSaled, String>



    @FXML
    private lateinit var priceCol: TableColumn<ProductSaled, String>

    @FXML
    private lateinit var qtyCol: TableColumn<ProductSaled, String>

    //


    @FXML
    private lateinit var qtyTotal: TextField

    @FXML
    private lateinit var grandTotal: TextField


     //

    @FXML
    private lateinit var qtyTextField: TextField

    @FXML
    private lateinit var tableView: TableView<ProductSaled>

    @FXML
    private lateinit var totalCol: TableColumn<ProductSaled, String>

    @FXML lateinit var customerCode:TextField

    @FXML lateinit var descriptionProductTf:TextField



    @FXML
    fun onDeleteProductButtonClick() {
        mProducts.remove(mProductSaledSelected)
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

        if(cashTextField.text.isNotEmpty()){
            changeTextField.text = (cashTextField.text.toDouble()-grandTotal.text.toDouble()).toString()
            println(" ${event.code.equals( KeyCode.ENTER )} && ${changeTextField.text.toDouble()>=0} ${event.code}")
            if ( event.code.equals( KeyCode.ENTER ) && changeTextField.text.toDouble()>=0 ){
                payBtn.requestFocus()
            }
        }
    }
    @FXML
    fun onPay() {
       pay()

    }

    private fun pay(){
        if( validatePayment() ){


                val codeBar = Random.nextLong(LongRange(10_000_000,99_999_999))
                InvoiceDao.save(
                    InvoiceEntity(mProducts, Context.currentEmployee.value, mCurrentCustomer, codeBar.toString(), Calendar.getInstance())
                ).subscribe({

                        mCurrentCustomer?.let {
                            val point = (grandTotal.text.toDouble()/100.0).toLong()
                            CustomerDao.updatePoint(it.id, point)

                        }

                        ViewUtil.createAlert(Alert.AlertType.CONFIRMATION,"Payment","Pay with success").showAndWait()
                        clearAll()
                        customerCode.requestFocus()

                },{})



        }
    }

    private fun validatePayment(): Boolean {
        if (mProducts.isEmpty()){
            ViewUtil.createAlert(Alert.AlertType.WARNING,"No Products","There is no products to pay").showAndWait()
            return false
        }
        if (cashTextField.text.isBlank() || cashTextField.text.toDouble()<grandTotal.text.toDouble() ){
            ViewUtil.createAlert(Alert.AlertType.WARNING,"Invalid Data","Cash must be greater than grand total").showAndWait()
            return false
        }
        return true
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

        if (event.code.equals( KeyCode.ENTER )){

            var code = (event.source as TextField).text
            code = code.padStart(12,'0')

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



    var mProducts: ObservableList<ProductSaled> = FXCollections.observableArrayList(  )
    var mProductSaledSelected:ProductSaled?=null

    private fun createTimeLine() {
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
    private val mMedia = Media(HelloApplication.javaClass.getResource("audio/beep7.mp3").toString())
    private var mMediaplayer = MediaPlayer(mMedia)

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

    /*private fun findProductBy(code:String){
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
    }*/
    private fun findProductBy(code:String){
        val tempCode = code.padStart(12,'0')
        val productFind:ProductSaled? = mProducts.find { it.code == tempCode }
        if (qtyTextField.text.isBlank()){
            qtyTextField.text = "1"
        }



        if (productFind!=null ){
            if ( isEnough(qtyWantBye = qtyTextField.text.toFloat(), productQty = productFind.qtyRemaining, codeProduct = tempCode, sellBy = productFind.sellBy) ){
                val index = mProducts.indexOf( productFind )
                productFind.qty += qtyTextField.text.toInt()
                mProducts[index] = productFind
                clearTextS()
                beep()
            }
        }
        else{
             ProductDetails.findProductByCode( codeProductTextField.text )
                 .subscribe(
                     {
                         if ( isEnough(qtyWantBye = qtyTextField.text.toFloat(), productQty = it.quantityRemaining, codeProduct = tempCode, sellBy = it.sellBy) ){
                             it.quantity = qtyTextField.text.toFloat()
                             val t = ProductSaled( it,false )
                             t.qtyRemaining = it.quantityRemaining
                             mProducts.add( t )
                             clearTextS()
                             beep()
                         }
                 },{
                     th-> println( th.message )
                   },
                     {

                     ViewUtil.createAlert(
                         Alert.AlertType.WARNING,
                         "Product not found",
                         "code prod. : $tempCode"
                     ).showAndWait()

                 })

        }

    }

    fun hideBottomPanelOnMouseClicked(){
        bottomPanelVBOx.changeVisibility()
    }

    private fun isEnough(qtyWantBye:Float, productQty:Float, codeProduct:String,sellBy:String):Boolean{

        val qtyBought = mProducts.filtered { it.code.equals( codeProduct ) }.sumOf { it.qty.toInt() }

        if (productQty<qtyBought+qtyWantBye){
            ViewUtil.createAlert(
                Alert.AlertType.INFORMATION,
                "Quantity don't enough",
                "There is only: $productQty and you already bought $qtyBought"
            ).showAndWait()
            return false
        }
        if (sellBy.equals("unit",true)){
            if(qtyWantBye % 1>0){
                ViewUtil.createAlert(
                    Alert.AlertType.WARNING,
                    "Bad data",
                    "This product is sold per unit."
                ).showAndWait()
                return false
            }

        }

        return true
    }

}
package com.sonderben.trust.controller

import com.sonderben.trust.CategoryEnum
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.controller.config.Admin
import com.sonderben.trust.controller.config.InvoiceController
import com.sonderben.trust.db.dao.EnterpriseDao
import entity.EnterpriseEntity
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.Pagination
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import java.net.URL
import java.time.ZoneId
import java.util.*

class ConfigurationController:Initializable, BaseController() {

    lateinit var mainPane: VBox
    private lateinit var enterprise:EnterpriseEntity
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        editMenuItem()
        HelloApplication.primary.isResizable = true





        if ( EnterpriseDao.enterprises.isNotEmpty() ){
            back.isVisible = true
            back.isManaged = true
            enterprise = EnterpriseDao.enterprises[0]
            pagination.setPageFactory { index ->createPage(index) }
        }else{
            back.isVisible = false
            back.isManaged = false
            enterprise = EnterpriseEntity()
            pagination.setPageFactory { index ->createPage(index) }
        }


         EnterpriseDao.enterprises.addListener (  ListChangeListener  {change->
             if (change.next()){
                 enterpriseInfo=null
                 nodeAdmin=null
                 invoice=null

                 back.isVisible = true
                 back.isManaged = true
                 enterprise = EnterpriseDao.enterprises[0]
                 pagination.setPageFactory { index ->createPage(index) }
             }

         } )


        pagination.pageCount = 2

    }

    private fun editMenuItem() {
        MainController.editMenu?.items?.clear()


        val saveMenuItem = MenuItem("Save")
        saveMenuItem.setOnAction {
            onSave()
        }
        val updateMenuItems = MenuItem("Update")
        updateMenuItems.setOnAction {

        }


        val clearMenuItems = MenuItem("Clear")
        clearMenuItems.setOnAction {
            clear(mainPane)
        }


        MainController.editMenu?.items?.addAll(saveMenuItem, updateMenuItems)

    }

    lateinit var screenTitle: Label



    @FXML
    private lateinit var pagination:Pagination
    @FXML
    private lateinit var back:ImageView
    private var admin:Admin?=null
    private val invoiceController:InvoiceController?=null

    private var nodeAdmin:Node?=null
    private var enterpriseInfo:Node?=null
    private var invoice:Node?=null
    private fun createPage(index :Int):Node{
        val resourcesBundle = ResourceBundle.getBundle("com.sonderben.trust.i18n.string")
        val fxmlLoader: FXMLLoader
        when(index){
            0 -> {
               if(nodeAdmin==null){
                    fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/config/admin.fxml"),resourcesBundle)
                   nodeAdmin = fxmlLoader.load<Node>()
                   admin = fxmlLoader.getController()
                   admin?.enterprise = enterprise

               }
                screenTitle.text = "Administrator"
                return nodeAdmin!!
            }
            else -> {
                if (invoice==null){
                     fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/config/invoice.fxml"),resourcesBundle)
                    invoice = fxmlLoader.load<Node>()
                    invoiceController?.enterprise = enterprise
                }
                screenTitle.text = "Setup your Invoice"
                return invoice!!
            }

        }


    }



    override fun onDestroy() {

    }

    fun backOnMouseClick() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
        HelloApplication.primary.scene = Scene(fxmlLoader.load(), 720.0, 440.0)
    }

    fun onSave() {
        enterprise.apply {
            name = admin!!.nameTextField.text
            telephone =  admin!!.telephoneTextField.text
            website =  admin!!.websiteTextField.text
            direction =  admin!!.directionTextField.text
            foundation =  GregorianCalendar.from( admin!!.foundationDatePicker.value.atStartOfDay(ZoneId.systemDefault()))
            category = CategoryEnum.valueOf(  admin!!.categoryChoiceBox.selectionModel.selectedItem.uppercase() )

            println("onsave buisiness info")
            employee?.apply {
                firstName = admin!!.firstNameTextField.text
                lastName = admin!!.lastNameTextField.text
                birthDay = GregorianCalendar.from(admin!!.birthdayDatePicker.value.atStartOfDay(ZoneId.systemDefault()))
                userName = admin!!.userNameTextField.text
                password = admin!!.passwordField.text
                passport = admin!!.passportTextField.text
                telephone = admin!!.telephoneTextField.text
                bankAccount = admin!!.accountNumberTextField.text
                genre = admin!!.choiceBoxGender.value
                email = admin!!.emailTextField.text
                direction = admin!!.directionField.text
            }

        }

        EnterpriseDao.save(enterprise)
            .subscribe({
                val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
                val scene = Scene(fxmlLoader.load(), 720.0, 440.0)
                HelloApplication.primary.scene = scene
            },{})
    }
}
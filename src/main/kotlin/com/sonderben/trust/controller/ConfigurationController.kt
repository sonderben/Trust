package com.sonderben.trust.controller

import SingletonView
import com.sonderben.trust.CategoryEnum
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.controller.config.Admin
import com.sonderben.trust.controller.config.BusinessInfoController
import com.sonderben.trust.controller.config.InvoiceController
import com.sonderben.trust.db.dao.EnterpriseDao
import entity.EnterpriseEntity
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Pagination
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import org.sqlite.ExtendedCommand.SQLExtension
import java.net.URL
import java.time.ZoneId
import java.util.*

class ConfigurationController:Initializable, BaseController() {

    private var enterprise:EnterpriseEntity? =null
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        HelloApplication.primary.isResizable = true
        setFromLoginPage(false)

        if (!fromLoginPage){
            try {
                enterprise = EnterpriseDao.enterprises[0]
            }catch (_:Throwable){
                enterprise = EnterpriseEntity()
            }
        }

        pagination.pageCount = 3
        pagination.setPageFactory { index ->createPage(index) }
    }

    lateinit var screenTitle: Label

    private var fromLoginPage = false

    @FXML
    private lateinit var pagination:Pagination
    @FXML
    private lateinit var back:ImageView
    var businessInfoController:BusinessInfoController?=null
    var admin:Admin?=null
    val invoiceController:InvoiceController?=null

    private var nodeAdmin:Node?=null
    private var enterpriseInfo:Node?=null
    private var invoice:Node?=null
    private fun createPage(index :Int):Node{
        val resourcesBundle = ResourceBundle.getBundle("com.sonderben.trust.i18n.string")
        val fxmlLoader: FXMLLoader
        when(index){
            0 -> {
               if(enterpriseInfo==null){
                    fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/config/businessInfo.fxml"),resourcesBundle)
                   enterpriseInfo = fxmlLoader.load<Node>()
                   businessInfoController=fxmlLoader.getController()
                   businessInfoController?.enterprise = enterprise

                   screenTitle.text = "Enterprise Info"
               }
                return enterpriseInfo!!
            }
            1 -> {
               if(nodeAdmin==null){
                    fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/config/admin.fxml"),resourcesBundle)
                   nodeAdmin = fxmlLoader.load<Node>()
                   admin = fxmlLoader.getController()

                   admin?.enterprise = enterprise

                   screenTitle.text = "Administrator"
               }
                return nodeAdmin!!
            }
            else -> {
                if (invoice==null){
                     fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/config/invoice.fxml"),resourcesBundle)
                    invoice = fxmlLoader.load<Node>()
                    invoiceController?.enterprise = enterprise

                    screenTitle.text = "Setup your Invoice"
                }
                return invoice!!
            }

        }


    }

    fun setFromLoginPage( v:Boolean ){
            fromLoginPage = v
            back.isVisible = v
            back.isManaged = v

    }

    override fun onDestroy() {

    }

    fun backOnMouseClick() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
        HelloApplication.primary.scene = Scene(fxmlLoader.load(), 720.0, 440.0)
    }

    fun onSave(actionEvent: ActionEvent) {



        enterprise?.apply {
            name = businessInfoController!!.nameTextField.text
            telephone =  businessInfoController!!.telephoneTextField.text
            website =  businessInfoController!!.websiteTextField.text
            direction =  businessInfoController!!.directionTextField.text
            foundation =  GregorianCalendar.from( businessInfoController!!.foundationDatePicker.value.atStartOfDay(ZoneId.systemDefault()))
            category = CategoryEnum.valueOf(  businessInfoController!!.categoryChoiceBox.selectionModel.selectedItem.uppercase() )

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

        EnterpriseDao.save( enterprise!! )
            .subscribe({
                val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
                val scene = Scene(fxmlLoader.load(), 720.0, 440.0)
                HelloApplication.primary.scene = scene
            },{})
    }
}
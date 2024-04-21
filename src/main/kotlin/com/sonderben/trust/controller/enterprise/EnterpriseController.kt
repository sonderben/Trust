package com.sonderben.trust.controller.enterprise

import com.sonderben.trust.CategoryEnum
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.LoginController
import com.sonderben.trust.constant.Constant
import com.sonderben.trust.controller.BaseController
import com.sonderben.trust.controller.MainController
import com.sonderben.trust.db.service.EnterpriseService
import com.sonderben.trust.viewUtil.ViewUtil
import entity.EnterpriseEntity
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
import javafx.scene.control.Pagination
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import java.net.URL
import java.time.ZoneId
import java.util.*

class EnterpriseController:Initializable, BaseController() {

    lateinit var saveButton: Button
    lateinit var mainPane: VBox
    private lateinit var enterprise:EnterpriseEntity
    private lateinit var resourceBundle: ResourceBundle

    override fun initialize(location: URL?, resources: ResourceBundle) {
        resourceBundle = resources
        editMenuItem()
        HelloApplication.primary.isResizable = true


        if ( EnterpriseService.getInstance().entities.isNotEmpty() ){
            back.isVisible = false
            back.isManaged = false
            enterprise = EnterpriseService.getInstance().entities[0]
            pagination.setPageFactory { index ->createPage(index) }
        }else{
            back.isVisible = true
            back.isManaged = true
            enterprise = EnterpriseEntity()
            pagination.setPageFactory { index ->createPage(index) }
            saveButton.text = resources.getString("save") ?: "Save"
        }


        pagination.pageCount = 2

    }

    private fun editMenuItem() {
        MainController.editMenu?.items?.clear()


        val saveMenuItem = MenuItem("Save")
        saveMenuItem.setOnAction {
            onSave()
        }
        /*val updateMenuItems = MenuItem("Update")
        updateMenuItems.setOnAction {

        }*/


        val clearMenuItems = MenuItem("Clear")
        clearMenuItems.setOnAction {
            clear(mainPane)
        }


        MainController.editMenu?.items?.addAll(saveMenuItem/*, updateMenuItems*/)

    }





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
                return nodeAdmin!!
            }
            else -> {
                if (invoice==null){
                     fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/config/invoice.fxml"),resourcesBundle)
                    invoice = fxmlLoader.load<Node>()
                    invoiceController?.enterprise = enterprise
                }
                return invoice!!
            }

        }


    }



    override fun onDestroy() {

    }

    fun backOnMouseClick() {
        val fxmlLoader = FXMLLoader( HelloApplication::class.java.getResource("login.fxml") ,Constant.resource)
        HelloApplication.primary.scene = Scene( fxmlLoader.load(), HelloApplication.primary.scene.width, HelloApplication.primary.scene.height )
    }

    fun onSave() {

        if (validateEmployee() ){
            val catString = getCategoryEnumFromIndex( admin!!.categoryChoiceBox.selectionModel.selectedIndex )

            enterprise.apply {
                name = admin!!.nameTextField.text
                telephone =  admin!!.telephoneTextField.text
                website =  admin!!.websiteTextField.text
                direction =  admin!!.directionTextField.text
                foundation =  GregorianCalendar.from( admin!!.foundationDatePicker.value.atStartOfDay(ZoneId.systemDefault()) )
                category = CategoryEnum.valueOf( catString )

                adminEntity?.apply {
                    firstName = admin!!.firstNameTextField.text
                    lastName = admin!!.lastNameTextField.text
                    birthDay = GregorianCalendar.from(admin!!.birthdayDatePicker.value.atStartOfDay(ZoneId.systemDefault()))
                    userName = admin!!.userNameTextField.text
                    password = admin!!.passwordField.text
                    telephone = admin!!.telephoneTextField.text
                    genre = admin!!.choiceBoxGender.value
                    email = admin!!.emailTextField.text
                }
            }

            if (EnterpriseService.getInstance().entities.isEmpty()){
                EnterpriseService.getInstance().save(enterprise)
                    .subscribe({

                        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"),resourceBundle)
                        val parent = fxmlLoader.load<Parent>()
                        val loginController = fxmlLoader.getController<LoginController>()
                        loginController.isFromEnterprise = true

                        val scene = Scene( parent,  HelloApplication.primary.scene.width, HelloApplication.primary.scene.height)
                        HelloApplication.primary.scene = scene
                    },{})
            }
            else{
                val loading = ViewUtil.loadingView()
                loading.show()
                EnterpriseService.getInstance().update(enterprise)
                    .subscribe({
                               loading.close()//
                    },{
                        th-> println(th.message)
                        loading.close()
                    })
            }
        }

    }

    private fun getCategoryEnumFromIndex(selectedIndex: Int): String {
        println("index: $selectedIndex")
        return when(selectedIndex){
            0 -> "SUPERMARKET"
            1 -> "HARDWARE_STORE"
            2 -> "PHARMACY"
            else -> "GENERAL"
        }
    }
    private fun validateEmployee():Boolean{
        if (admin!!.nameTextField.text.isBlank()){
            ViewUtil.customAlert(ViewUtil.WARNING,"please make sure you fill out all the text fields.").show()
            return false
        }
        if (admin!!.telephoneTextField.text.isBlank()){
            ViewUtil.customAlert(ViewUtil.WARNING,"please make sure you enter a international phone number.").show()
            return false
        }
        if ( admin!!.directionTextField.text.isBlank() ){
            ViewUtil.customAlert(ViewUtil.WARNING,"please make sure you enter the direction.").show()
            return false
        }
        if (admin!!.foundationDatePicker.value == null){
            ViewUtil.customAlert(ViewUtil.WARNING,"please make sure you enter the foundation date.").show()

            return false
        }
        if (admin!!.firstNameTextField.text.isBlank()){
            ViewUtil.customAlert(ViewUtil.WARNING,"please enter your first name.").show()

            return false
        }
        if ( admin!!.lastNameTextField.text.isBlank() ){
            ViewUtil.customAlert(ViewUtil.WARNING,"please enter your last name.").show()
            return false
        }
        if ( admin!!.birthdayDatePicker.value == null ){
            ViewUtil.customAlert(ViewUtil.WARNING,"please enter a birthday.").show()
            return false
        }
        if ( admin!!.userNameTextField.text.isBlank() ){
            ViewUtil.customAlert(ViewUtil.WARNING,"please make sure you fill out all the text fields.").show()
            return false
        }
        if ( admin!!.passwordField.text.isBlank() ){
            ViewUtil.customAlert(ViewUtil.WARNING,"please make sure you enter a valid password.").show()
            return false
        }

        if ( admin!!.telephoneTextField.text.isBlank() ){
            ViewUtil.customAlert(ViewUtil.WARNING,"please enter a valid international phone number.").show()
            return false
        }

        if ( admin!!.emailTextField.text.isBlank() ){
            ViewUtil.customAlert(ViewUtil.WARNING,"please make sure you enter a valid email.").show()
            return false
        }

        if ( admin!!.choiceBoxGender.value == null ){
            ViewUtil.customAlert(ViewUtil.WARNING,"please make sure you select a gender.").show()
            return false
        }
        return true
    }
}
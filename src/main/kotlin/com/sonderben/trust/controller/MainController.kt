package com.sonderben.trust.controller

import SingletonView
import com.sonderben.trust.Context
import com.sonderben.trust.HelloApplication
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*


class MainController : Initializable {
    private fun selecttAppropriateView(idNode: String) {
        val selected = vboxLateral.children.filter { it.id.lowercase() == idNode }
        if (selected.isNotEmpty())
            onClickLateralButton( selected[0] )
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        //HelloApplication.primary.isResizable = true
        hideLeftPanelOnMouseClicked()

        next = nextPage
        forward = forwardPage

        editMenu = menuEdit
        bottomBarMenuItem = bottomBar


        //menuBar.useSystemMenuBarProperty().set(true)
        screensByRole()
        if (vboxLateral.children.size ==1 ){
            borderpane.left = null
            onClickLateralButton( vboxLateral.children[0] )
        }
        if ( vboxLateral.children.size >0 ){
            changeView("view/sale.fxml")
            onClickLateralButton( vboxLateral.children[0] )
            onSelect()
        }

        for (items in vboxLateral.children){
            val r = (items as HBox).children[1] as Label

            val menuItem = MenuItem(r.text)
            menuItem.id = "${items.id}_"



            when ( menuItem.id.lowercase() ) {
                "sale_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT1,KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/sale.fxml")
                        selecttAppropriateView( "sale" )
                    }
                }
                "product_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT2,KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/product.fxml")
                        selecttAppropriateView( "product" )
                    }
                }
                "employee_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT3,KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/employee.fxml")
                        selecttAppropriateView( "employee" )
                    }
                }
                "configuration_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT4,KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/configuration.fxml")
                        selecttAppropriateView( "configuration" )
                    }
                }

                "role_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT5,KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/role.fxml")
                        selecttAppropriateView( "role" )
                    }
                }
                "queries_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT6,KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/queries/queries.fxml")
                        selecttAppropriateView( "queries" )
                    }
                }
                "customer_service_"->{
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT7,KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/customerService.fxml")
                        selecttAppropriateView( "customer_service" )
                    }
                }
            }

            menuView.items.addAll(
                menuItem
            )
        }


    }


    lateinit var bottomBar: MenuItem
    //lateinit var bottombar: MenuItem



    @FXML
    private lateinit var vboxLateral: VBox

    @FXML
    private lateinit var stackPane: StackPane
    @FXML
    private lateinit var borderpane: BorderPane

    @FXML
    private lateinit var menuBar: MenuBar
    @FXML
    private lateinit var topBarHBox: HBox
    @FXML
    private lateinit var menuEdit: Menu

    @FXML
    private lateinit var nextPage:ImageView
    @FXML
    private lateinit var forwardPage: ImageView

    @FXML
    private lateinit var menuView:Menu



    companion object {
          var next:ImageView?=null
          var forward:ImageView?=null
          var editMenu: Menu?=null
          var bottomBarMenuItem:MenuItem?=null

        fun hideBottomBar(hide:Boolean,onAction:( ()->Unit )?=null){
            if (bottomBarMenuItem!=null){
                bottomBarMenuItem!!.isDisable = hide

                if (!hide)
                    bottomBarMenuItem!!.setOnAction {
                        onAction?.invoke()
                    }
            }

        }
    }





    private fun onSelect() {
        vboxLateral.children.forEachIndexed { _, it ->
            it.setOnMouseClicked { event ->
                if (event != null) {
                    onClickLateralButton(event.source as Node)
                    when (it.id.lowercase()) {
                        "sale" -> changeView("view/sale.fxml")
                        "product" -> changeView("view/product.fxml")
                        "employee" -> changeView("view/employee.fxml")
                        "enterprise" -> changeView("view/configuration.fxml")
                        "inventory" -> changeView("view/inventory.fxml")
                        "role" -> changeView("view/role.fxml")
                        "queries" -> changeView("view/queries/queries.fxml")
                        "customer_service"->changeView("view/customerService.fxml")
                    }
                }
            }
        }
    }

    private fun changeView(relativeUrl: String) {
        val a = SingletonView.get(relativeUrl)
        if (a != null && !stackPane.children.contains(a)) {
            stackPane.children.clear()
            stackPane.children.add(a)
        }
        //a?.toFront()
    }

    private fun screensByRole(){
        val screensIdEmployee:List<String> = Context.currentEmployee.value.role.screens.map { screen -> screen.name.lowercase() }
        vboxLateral.children.removeIf { !screensIdEmployee.contains(it.id.lowercase()) }
    }


    private fun onClickLateralButton(node:Node ){
        vboxLateral.children.forEach {
            if (node === it){
                it.style = "-fx-background-color: #FFFFFF35"
            }else{
                it.style = ""
            }
        }
    }

    private  var xOffset = 0.0
    private  var yOffset = 0.0
    fun topBarHBoxOnMousePressed(event: MouseEvent) {
        xOffset = event.screenX - HelloApplication.primary.x
        yOffset = event.screenY - HelloApplication.primary.y
    }

    fun topBarHBoxOnMouseDragged(event:MouseEvent) {
        HelloApplication.primary.x = event.screenX - xOffset
        HelloApplication.primary.y = event.screenY - yOffset
    }





    fun  hideLeftPanelOnMouseClicked() {
        if (borderpane.left.isVisible){
            borderpane.left.isVisible = false
            borderpane.left.managedProperty().set(false)
        }else{
            borderpane.left.isVisible = true
            borderpane.left.managedProperty().set(true)
        }

    }

    @FXML
    fun lateralBarOnAction() {
        if (borderpane.left.isVisible){
            borderpane.left.isVisible = false
            borderpane.left.managedProperty().set(false)
        }else{
            borderpane.left.isVisible = true
            borderpane.left.managedProperty().set(true)
        }
    }

    @FXML fun disconnectOnMouseClicked() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
        val scene = Scene(fxmlLoader.load(), 720.0, 440.0)
        HelloApplication.primary.scene = scene
    }

}
package com.sonderben.trust.controller

import SingletonView
import com.sonderben.trust.Context
import com.sonderben.trust.HelloApplication
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.MenuBar
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Screen
import javafx.stage.Stage
import java.net.URL
import java.util.*


class MainController : Initializable {
    override fun initialize(location: URL?, resources: ResourceBundle?) {


        next = nextPage
        forward = forwardPage


        menuBar.useSystemMenuBarProperty().set(true)
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




    }
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
    private lateinit var nextPage:ImageView
    @FXML
    private lateinit var forwardPage: ImageView



    companion object {

        lateinit var next:ImageView
        lateinit var forward:ImageView


    }





    private fun onSelect() {
        vboxLateral.children.forEachIndexed { index, it ->
            it.setOnMouseClicked { event ->
                if (event != null) {
                    onClickLateralButton(event.source as Node)
                    when (it.id.lowercase()) {
                        "sale" -> changeView("view/sale.fxml")
                        "product" -> changeView("view/product.fxml")
                        "employee" -> changeView("view/employee.fxml")
                        "configuration" -> changeView("view/configuration.fxml")
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
        val screensIdEmployee:List<String> = Context.currentEmployee.value.role.screens.map { screen -> screen.screen.name.lowercase() }
        vboxLateral.children.removeIf { !screensIdEmployee.contains(it.id.lowercase()) }
    }


    private fun onClickLateralButton(node:Node ){


        vboxLateral.children.forEach {
            if (node === it){
                //032D3BFF
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

    fun disconnectOnMouseClicked(mouseEvent: MouseEvent) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"))
        val scene = Scene(fxmlLoader.load(), 720.0, 440.0)
        HelloApplication.primary.scene = scene
    }

}
package com.sonderben.trust.controller

import com.sonderben.trust.HelloApplication
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Pagination
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class ConfigurationController:Initializable, BaseController() {
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        HelloApplication.primary.isResizable = true
        setFromLoginPage(false)
        pagination.pageCount = 3
        pagination.setPageFactory { index ->createPage(index) }
    }

    lateinit var screenTitle: Label

    private var fromLoginPage = false

    @FXML
    private lateinit var pagination:Pagination
    @FXML
    private lateinit var back:ImageView
    private fun createPage(index :Int):Node{
        when(index){
            0 -> {
                screenTitle.text = "Enterprise Info"
                return SingletonView.get("view/config/businessInfo.fxml")
            }
            1 -> {
                screenTitle.text = "Administrator"
                return SingletonView.get("view/config/admin.fxml")
            }
            2 -> {
                screenTitle.text = "Setup your Invoice"
                return SingletonView.get("view/config/invoice.fxml")
            }
        }
        val vbox = VBox()
        vbox.children.add( Label("Chen") )
        return vbox

    }

    fun setFromLoginPage( v:Boolean ){

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

    }
}
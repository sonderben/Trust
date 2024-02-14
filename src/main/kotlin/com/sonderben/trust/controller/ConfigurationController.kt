package com.sonderben.trust.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.Pagination
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class ConfigurationController:Initializable, BaseController() {
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        pagination.pageCount = 3
        pagination.setPageFactory { index ->createPage(index) }
    }

    @FXML
    private lateinit var pagination:Pagination
    private fun createPage(index :Int):Node{
        when(index){
            0 -> return SingletonView.get("view/config/businessInfo.fxml")
            1 -> return SingletonView.get("view/config/admin.fxml")
            2 -> return SingletonView.get("view/config/invoice.fxml")
        }
        val vbox = VBox()
        vbox.children.add( Label("Chen") )
        return vbox

    }

    override fun onDestroy() {

    }
}
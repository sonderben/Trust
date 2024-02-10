package com.sonderben.trust.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class ConfigurationController:Initializable {
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        onSelect()
        val business = 9

    }
    private fun onSelect() {
        lateralVboxConfig.children.forEachIndexed { index, it ->
            it.setOnMouseClicked { event ->
                if (event != null) {
                    onClickLateralButton(event.source as Node)
                    println("id: ${it.id.lowercase()}")
                    when (it.id.lowercase()) {
                        "businessconf" -> changeView("view/config/businessInfo.fxml")
                        "invoiceconf" -> changeView("view/config/invoice.fxml")
                    }
                }
            }
        }
    }
    private fun changeView(relativeUrl: String) {
        val a = SingletonView.get(relativeUrl)


        if (a != null && !stackPaneConfig.children.contains(a)) {
            stackPaneConfig.children.add(a)
        }
        a?.toFront()
    }
    private fun onClickLateralButton(node: Node) {
        lateralVboxConfig.children.forEach {
            if (node === it){
                it.style = "-fx-background-color: #032d3b"
            }else{
                it.style = ""
            }
        }
    }

    @FXML
    private lateinit var buisinessConf: HBox

    @FXML
    private lateinit var lateralVboxConfig: VBox

    @FXML
    private lateinit var stackPaneConfig: StackPane
}
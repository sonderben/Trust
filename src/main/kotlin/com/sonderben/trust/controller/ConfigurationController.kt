package com.sonderben.trust.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class ConfigurationController:Initializable {
    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

    @FXML
    private lateinit var buisinessConf: HBox

    @FXML
    private lateinit var lateralVboxConfig: VBox

    @FXML
    private lateinit var stackPaneConfig: StackPane
}
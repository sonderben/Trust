package com.sonderben.trust.controller

import com.sonderben.model.Technology
import javafx.fxml.Initializable
import javafx.scene.control.ListView
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import java.net.URL
import java.util.*

class TechnologiesController: Initializable, BaseController() {
    lateinit var webview: WebView
    lateinit var listview: ListView<Technology>


    override fun initialize(p0: URL?, p1: ResourceBundle?) {


        val mutableList = mutableListOf( Technology("RXJava",""),Technology("RXJava",""),Technology("RXJava","") )
        val w:WebEngine=webview.engine
        w.load("https://www.bendersonphanor.site/")
        listview.items.addAll( mutableList)
        listview.selectionModel.selectedItemProperty().addListener { stringObservableValue, old, new ->

            println("cool: "+new)
        }
    }

    override fun onDestroy() {

    }
}
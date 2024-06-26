package com.sonderben.trust.controller.enterprise

import com.sonderben.trust.Context
import entity.EnterpriseEntity
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class InvoiceController:Initializable {
    lateinit var vboxButtons: VBox
    var enterprise: EnterpriseEntity?=null


    @FXML
    private lateinit var mainhbox:HBox
    @FXML
    private lateinit var htmlEditor:HTMLEditor





    override fun initialize(location: URL?, resources: ResourceBundle?) {


        htmlEditor.style = "-fx-max-width: 80mm;"



        vboxButtons.children.forEach {
            it.setOnMouseClicked {
                val btn = it.source as Button
                when(btn.id){
                    ""->setHtmlEditor("")
                    else->setHtmlEditor(btn.text)
                }
            }
        }



        val first = htmlEditor.lookup(".top-toolbar")
        val bottom = htmlEditor.lookup(".bottom-toolbar") as ToolBar
        println(first !=null)
        if (first !=null){
            /*first.isVisible = false
            first.isManaged = false
            bottom.isVisible = false
            bottom.isManaged = false*/
        }


        /*println(bottom.items.size)

        bottom.items.addListener(ListChangeListener { change->
            val container = bottom.lookup(".container") as HBox?
            val dd = bottom.lookup(".tool-bar-overflow-button")
            if (container!=null){
                bottom.items.remove(container)
                container.isVisible = false
                container.isManaged = false
            }else{
                println("container is null")
            }
        })*/




        readContent(htmlEditor)
        htmlEditor.addEventFilter(KeyEvent.KEY_TYPED){
            //ScenicView.show(htmlEditor)
            /*val caretPosition = textArea.caretPosition
            val currentLine =textArea.text.substring(0,caretPosition).lines().last()
            if (currentLine.length >= MAX_COLUMN){
                event.consume()
            }*/



            saveContent(htmlEditor.htmlText)
        }



    }

    private fun setHtmlEditor(text: String) {
        val html = htmlEditor.htmlText

    }


    private fun getPositionCaret(): Any {
        val webview:WebView=htmlEditor.lookup(".web-view") as WebView
        val webEngine = webview.engine
        val index = webEngine.executeScript(" window.getSelection().getRangeAt(0).startOffset")
        return index
    }


    fun saveContent(htmlEditor:String) {
        val file = File("data/invoice.html")
        try {
            val fileWritter = FileWriter(file)
            fileWritter.use {
                fileWritter.write(htmlEditor)
            }

        }catch (ex: IOException){
            ex.printStackTrace()
        }
    }
    private fun readContent(htmlEditor: HTMLEditor) {
        try {
            val file = File("data/invoice.html")
            if (!file.exists()){
                file.createNewFile()
                saveContent(Context.defaultInvoice( ))
            }
            val t= String( Files.readAllBytes( Paths.get( "data/invoice.html" ) ) )
            htmlEditor.htmlText = t

        }catch (ex: IOException){
            ex.printStackTrace()

        }

    }




}

package com.sonderben.trust.controller.config

//import com.gluonhq.richtextarea.RichTextArea
import com.sonderben.trust.controller.BaseController
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.print.Paper
import javafx.scene.Node
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextArea
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.web.HTMLEditor
import org.scenicview.ScenicView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class InvoiceController:Initializable {
    /*@FXML
    private lateinit var textArea: TextArea*/

    @FXML
    private lateinit var mainhbox:HBox
    @FXML
    private lateinit var htmlEditor:HTMLEditor

    val MAX_COLUMN = 32



    override fun initialize(location: URL?, resources: ResourceBundle?) {


        val first = htmlEditor.lookup(".tool-bar")
        val gridPane = htmlEditor.lookup(".grid") as GridPane
        //gridPane.children.clear()
        println(first !=null)
        if (first !=null){
            first.isVisible = false
            first.isManaged = false
        }




        readContent()
        htmlEditor.addEventFilter(KeyEvent.KEY_TYPED){ event->
            //ScenicView.show(htmlEditor)
            /*val caretPosition = textArea.caretPosition
            val currentLine =textArea.text.substring(0,caretPosition).lines().last()
            if (currentLine.length >= MAX_COLUMN){
                event.consume()
            }*/
            saveContent()
        }




    }





    private fun saveContent() {
        val file = File("data/invoice.html")
        try {
            val fileWritter = FileWriter(file)
            fileWritter.use {
                fileWritter.write(htmlEditor.htmlText)
            }

        }catch (ex:IOException){
            ex.printStackTrace()
        }
    }
    private fun readContent() {
        try {
            val temp = String( Files.readAllBytes( Paths.get( "data/invoice.html" ) ) )
            htmlEditor.htmlText = temp

        }catch (ex: IOException){
            ex.printStackTrace()
        }

    }




}

package com.sonderben.trust.controller.config

//import com.gluonhq.richtextarea.RichTextArea
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.print.Paper
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextArea
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.web.HTMLEditor
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

        textArea.isVisible = false
        textArea.isManaged = false

        readContent()
        htmlEditor.addEventFilter(KeyEvent.KEY_TYPED){ event->
            val caretPosition = textArea.caretPosition
            val currentLine =textArea.text.substring(0,caretPosition).lines().last()
            if (currentLine.length >= MAX_COLUMN){
                event.consume()
            }
            saveContent()
        }




    }

    @FXML
    private lateinit var textArea: TextArea



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

    fun alignRightMouseClicked(mouseEvent: MouseEvent) {

    }

    fun alignCenterMouseClicked(mouseEvent: MouseEvent) {

    }

    fun alignLeftMouseClicked(mouseEvent: MouseEvent) {

    }

    fun boldMouseClicked(mouseEvent: MouseEvent) {
        val selectedText = textArea.selectedText
        if (selectedText.isNotBlank()){
            val currentText = textArea.text
            val lines = selectedText.lines()


            var dd = ""
            for (i in 0 until lines.size){
                val length:Int = (MAX_COLUMN-lines.size )/2

                if (length>0){

                    dd += lines[i].padStart(length+lines[i].length,'*').padEnd(MAX_COLUMN,'*')
                    dd += "\n"

                }

            }
            dd=dd.dropLast(1)


            val newTextArea = currentText.replace(selectedText,dd)
            textArea.text = newTextArea
        }
        //println(selectedText)
    }
}

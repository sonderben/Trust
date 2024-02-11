package com.sonderben.trust.controller.config

//import com.gluonhq.richtextarea.RichTextArea
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.print.Paper
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextArea
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import java.net.URL
import java.util.*

class InvoiceController:Initializable {
    /*@FXML
    private lateinit var textArea: TextArea*/

    @FXML
    private lateinit var mainhbox:HBox
    @FXML

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        textArea.addEventFilter(KeyEvent.KEY_TYPED){event->
            val caretPosition = textArea.caretPosition
            val currentline =textArea.text.substring(0,caretPosition).lines().last()
            if (currentline.length>=12){
                event.consume()
            }
        }


    }

    @FXML
    private lateinit var textArea: TextArea



    /*private fun saveContent() {
        val file = File("data/res.html")
        try {
            val fileWritter = FileWriter(file)
            fileWritter.use {
                fileWritter.write(textArea.text)
            }

        }catch (ex:IOException){
            ex.printStackTrace()
        }
    }*/
    /*private fun readContent() {
        try {
            val temp = String( Files.readAllBytes( Paths.get( "data/res.html" ) ) )
            textArea.text = temp

        }catch (ex:IOException){
            ex.printStackTrace()
        }

    }*/
}

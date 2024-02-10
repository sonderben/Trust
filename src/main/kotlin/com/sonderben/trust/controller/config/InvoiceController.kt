package com.sonderben.trust.controller.config

import com.gluonhq.richtextarea.RichTextArea
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.print.Paper
import javafx.scene.control.ChoiceBox
import javafx.scene.layout.HBox
import java.net.URL
import java.util.*

class InvoiceController:Initializable {
    /*@FXML
    private lateinit var textArea: TextArea*/

    @FXML
    private lateinit var mainhbox:HBox
    @FXML
    private lateinit var richTextArea:RichTextArea
    val paper = Paper.A4
    override fun initialize(location: URL?, resources: ResourceBundle?) {



        richTextArea.prefWidth = (80 * 72 / 25.4 + 0.5)
        richTextArea.prefHeight = paper.height
        //mainhbox.children.add(0,richTextArea)

    }

    @FXML
    private lateinit var sizePaperCb: ChoiceBox<Any>

    @FXML
    fun onRotate(event: ActionEvent) {

        if (paper.equals( Paper.A4 )){
            if (richTextArea.prefWidth==paper.height){

            }
        }

        richTextArea.prefWidth = paper.height

    }

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
    }
    private fun readContent() {
        try {
            val temp = String( Files.readAllBytes( Paths.get( "data/res.html" ) ) )
            textArea.text = temp

        }catch (ex:IOException){
            ex.printStackTrace()
        }

    }*/
}

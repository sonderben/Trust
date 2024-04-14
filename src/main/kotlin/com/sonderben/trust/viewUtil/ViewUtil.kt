package com.sonderben.trust.viewUtil

import com.sonderben.trust.model.Technology
import com.sonderben.trust.HelloApplication
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle

class ViewUtil {
    companion object{
        fun createAlert(alertType: Alert.AlertType, title:String, headerText:String):Alert{
            val alert = Alert(alertType/*Alert.AlertType.WARNING*/)
            alert.initModality(Modality.APPLICATION_MODAL)
            alert.initOwner(HelloApplication.primary)
            alert.title = title
            alert.headerText = headerText
            return alert
        }

        fun loadingView():Stage{
            val loading = Stage()

            val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/loadingview.fxml"))
            val vbox:VBox = fxmlLoader.load()
            vbox.style = "-fx-background-color:transparent;"
            val scene = Scene(vbox, 200.0, 200.0)

            scene.fill = Color.TRANSPARENT
            scene.stylesheets.add("-fx-background-color:rgba(0,0,0,0.5);")



            loading.initStyle(StageStyle.TRANSPARENT)
            loading.initOwner(HelloApplication.primary)
            loading.initModality( Modality.APPLICATION_MODAL )
            loading.scene = scene
            loading.isAlwaysOnTop = true

            return loading
        }

        fun technology():Stage{
            val loading = Stage()

            val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/technologies.fxml"))
            val vbox:AnchorPane = fxmlLoader.load()
            val listView = vbox.children.filter{it.id=="listview"}[0] as ListView<Technology>


            listView.setCellFactory {
                object : ListCell<Technology?>() {
                    override fun updateItem(technology: Technology?, b: Boolean) {
                        super.updateItem(technology, b)
                        if (technology != null) {
                            text = technology.title
                        }
                    }
                }

            }
            listView.selectionModel.selectedItemProperty().addListener{observable,oldValue,newValue->
                HelloApplication.services.showDocument( newValue.url )
            }

            val mutableList = mutableListOf(

                Technology("RXJava","https://www.github.com/ReactiveX/RxJava") ,
                Technology("SQLite","https://www.sqlite.org"),
                Technology("JSON-simple","https://code.google.com/archive/p/json-simple"),

                Technology("Javafx","https://gluonhq.com/products/javafx"),
                Technology("Nashorn","docs.oracle.com/en/java/11/nashorn/introduction.html"),
                Technology("JUnit","https://junit.org/junit5/")
            )


            listView.items.addAll( mutableList)

            val scene = Scene(vbox, 400.0, HelloApplication.primary.height * 0.80)



            //loading.initOwner(HelloApplication.primary)
            loading.initModality( Modality.APPLICATION_MODAL )
            loading.scene = scene
            loading.isAlwaysOnTop = true

            return loading
        }

        fun customAlert(title:String,message:String,onClick:(()->Unit)?=null):Stage{
            val loading = Stage()

            val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/customAlert.fxml"))
            val vbox:VBox = fxmlLoader.load()

            //val node: GridPane = (vbox.children[0] as GridPane)
            val header = vbox.children.filter { it.id=="header" }[0] as Label
            val body = vbox.children.filter { it.id=="body" }[0] as Text

            header.text = title
            body.text = message

            val btn:Button = vbox.children.filterIsInstance<HBox>()[0].children.filter { it.id == "okBtn" }[0] as Button
            btn.setOnMouseClicked {
                onClick?.invoke()
                loading.close()
            }
            vbox.style = "-fx-background-color:#032d3b;"
            val scene = Scene(vbox, 400.0, 200.0)

            scene.fill = Color.TRANSPARENT
            scene.stylesheets.add("-fx-background-color:rgba(0,0,0,0.5);")



            loading.initStyle(StageStyle.UNDECORATED)
            loading.initOwner(HelloApplication.primary)
            loading.initModality( Modality.APPLICATION_MODAL )
            loading.scene = scene
            loading.isAlwaysOnTop = true

            return loading
        }


    }
}
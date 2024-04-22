package com.sonderben.trust.viewUtil

import com.sonderben.trust.model.Technology
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.center
import com.sonderben.trust.constant.Constant
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
import java.util.*

class ViewUtil {

    companion object{
        val WARNING:Int = 2
        val ERROR:Int = 3
        val INFO:Int = 1
        val SUCCESS:Int = 0


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

            val center = center()
            val centerX = center.x
            val centerY = center.y
            loading.x = centerX - (loading.scene.width * 0.50)
            loading.y = centerY - (loading.scene.height * 0.50)

            return loading
        }

        fun technology():Stage{
            val loading = Stage()
            loading.icons.add(
                Factory.createImage("image/shield.png")
            )
            loading.title = Constant.resource.getString("technologies")

            val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/technologies.fxml"),Constant.resource)
            val vbox:VBox = fxmlLoader.load()
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



            loading.initOwner(HelloApplication.primary)
            loading.initModality( Modality.APPLICATION_MODAL )
            loading.scene = scene
            loading.isAlwaysOnTop = true

            val center = center()
            val centerX = center.x

            loading.x = centerX - (loading.scene.width/2)

            return loading
        }

        fun customAlert(alertType:Int,message:String,onClick:(()->Unit)?=null):Stage{
            val loading = Stage()
            loading.icons.add(
                Factory.createImage("image/shield.png")
            )
            val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("view/customAlert.fxml"),Constant.resource)
            val mainVbox:VBox = fxmlLoader.load()



            val body = (mainVbox.children[0] as VBox).children[0] as Text
            body.style = createStyle( alertType )


            body.text = message.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            val btn:Button = mainVbox.children.filterIsInstance<HBox>()[0].children.filter { it.id == "okBtn" }[0] as Button
            btn.setOnMouseClicked {
                onClick?.invoke()
                loading.close()
            }
            mainVbox.style = "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #032d3b,#3498db);"
            val scene = Scene(mainVbox, 400.0, 200.0)

            val title =  when(alertType){
                INFO->  "info"
                WARNING->  "warning"
                SUCCESS -> "success"
                else->  "error"
            }
            loading.title = Constant.resource.getString( title )
            loading.initOwner(HelloApplication.primary)
            loading.initModality( Modality.APPLICATION_MODAL )
            loading.scene = scene
            loading.isAlwaysOnTop = HelloApplication.primary.isAlwaysOnTop

            loading.isResizable = false
            loading.isMaximized = false

            val center = center()
            val centerX = center.x
            val centerY = center.y
            loading.x = centerX - (loading.scene.width/2)
            loading.y = centerY - (loading.scene.height*0.40)

            btn.requestFocus()


            return loading
        }

        private fun createStyle(alertType: Int): String {

            return when(alertType){
                INFO ->  "-fx-fill: linear-gradient(from 25% 25% to 100% 100%, #C8C8C8,#A3A3A3);"
                WARNING -> "-fx-fill: linear-gradient(from 25% 25% to 100% 100%, #FFC62A,#C69A21);"
                SUCCESS -> "-fx-fill: linear-gradient(from 25% 25% to 100% 100%, #289858,#1B673C);"
                else ->  "-fx-fill: linear-gradient(from 25% 25% to 100% 100%, #FF4642,#FF5B3D);"
            }
        }


    }
}
package com.sonderben.trust.controller.queries

import com.sonderben.trust.controller.MainController
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class Queries : Initializable,EventHandler<MouseEvent> {
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        MainController.next.onMouseClicked = this
        MainController.forward.onMouseClicked = this


        vboxContainer.children.forEach { node ->
            if (node is FlowPane){
                node.children.forEach { vb ->
                    if (vb is VBox){
                        vb.onMouseClicked = this
                    }
                }
            }

        }



    }

    @FXML
    private lateinit var bestEmployee: VBox

    @FXML
    private lateinit var customerSpendMoney: VBox

    @FXML
    private lateinit var vboxContainer: VBox

    @FXML
    private lateinit var mainLayoutStackPane: StackPane

    @FXML
    private lateinit var productBestSelling: VBox

    @FXML
    private lateinit var productExpired: VBox

    @FXML
    private lateinit var productPurchasingCustomer: VBox

    @FXML
    private lateinit var productRemaining: VBox

    @FXML
    private lateinit var productSold: VBox

    @FXML
    private lateinit var returnedProduct: VBox
    private lateinit var pageBefore:Node


    override fun handle(event: MouseEvent?) {
        if (event !=null){
            if (event.eventType.equals( MouseEvent.MOUSE_CLICKED)){

                if ( event.source is VBox ){
                    val path = "view/queries/${(event.source as VBox).id}.fxml"
                    println(path)
                   if(SingletonView.get(path)!=null){
                       pageBefore = SingletonView.get(path)

                       mainLayoutStackPane.children.add( pageBefore )
                       MainController.forward.opacity =1.0
                   }
                }else if ( (event.source as Node).id.equals("forwardPage") ){

                    mainLayoutStackPane.children.remove(pageBefore)
                    MainController.forward.opacity = 0.26
                    MainController.next.opacity = 1.0
                }else if ( (event.source as  Node).id.equals("nextPage") ){

                    mainLayoutStackPane.children.add(pageBefore)
                    MainController.forward.opacity = 1.00
                    MainController.next.opacity = 0.26
                }

            }


        }


    }
}
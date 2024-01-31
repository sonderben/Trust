package com.sonderben.trust.controller

import SingletonView
import com.sonderben.trust.Context
import com.sonderben.trust.constant.Action
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.db.dao.CategoryDao
import com.sonderben.trust.model.Role
import com.sonderben.trust.model.Screen
import dto.EmployeeDto
import dto.ProductDto
import entity.CategoryEntity
import entity.EmployeeEntity
import entity.ProductEntity
import entity.ScheduleEntity
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*


class MainController : Initializable {
    override fun initialize(location: URL?, resources: ResourceBundle?) {

        screensByRole()
        if (vboxLateral.children.size ==1 ){
            borderpane.left = null
            onClickLateralButton( vboxLateral.children[0] )
        }
        if ( vboxLateral.children.size >0 ){
            changeView("view/sale.fxml")
            onClickLateralButton( vboxLateral.children[0] )
            onSelect()
        }




    }
    @FXML
    private lateinit var vboxLateral: VBox

    @FXML
    private lateinit var stackPane: StackPane
    @FXML
    private lateinit var borderpane: BorderPane



    private fun onSelect() {
        vboxLateral.children.forEachIndexed { index, it ->
            it.setOnMouseClicked { event ->
                if (event != null) {
                    onClickLateralButton(event.source as Node)
                    println("id: ${it.id.lowercase()}")
                    when (it.id.lowercase()) {
                        "sale" -> changeView("view/sale.fxml")
                        "product" -> changeView("view/product.fxml")
                        "employee" -> changeView("view/employee.fxml")
                        "configuration" -> changeView("view/configuration.fxml")
                    }
                }
            }
        }
    }

    private fun changeView(relativeUrl: String) {
        val a = SingletonView.get(relativeUrl)


        if (a != null && !stackPane.children.contains(a)) {
            stackPane.children.add(a)
        }
        a?.toFront()
    }

    private fun screensByRole(){
        val screensIdEmployee:List<String> = Context.currentEmployee.value.role.screens.map { screen -> screen.screen.name.lowercase() }
        vboxLateral.children.removeIf { !screensIdEmployee.contains(it.id.lowercase()) }
    }


    private fun onClickLateralButton(node:Node ){

        vboxLateral.children.forEach {
            if (node === it){
                it.style = "-fx-background-color: #032d3b"
            }else{
                it.style = ""
            }
        }
    }


}
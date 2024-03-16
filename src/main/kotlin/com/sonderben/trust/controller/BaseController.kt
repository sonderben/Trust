package com.sonderben.trust.controller

import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.DatePicker
import javafx.scene.control.MenuItem
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox

abstract class BaseController {

    abstract fun onDestroy()
    fun clear(pane: Pane){

            for (node in pane.children){

                when(node){
                    is TextField -> node.text = ""
                    is ChoiceBox<*> -> node.selectionModel.clearSelection()
                    is TableView<*> -> node.selectionModel.clearSelection()
                    is DatePicker -> node.value = null
                    is GridPane,is VBox,is HBox  -> clear(node as Pane)
                }

            }
    }

     fun enableActionButton(pane: Pane, tableViewItemIsSelected: Boolean){

         for (node in pane.children){

             if ( node is GridPane || node is VBox || node is HBox){
                 enableActionButton(node as Pane,tableViewItemIsSelected)
             }
             else if (node is Button){
                val temp = node.id.split("_").filter { it.contains("Btn") }
                 if (temp.isNotEmpty()){
                     when( temp[0] ){
                         "saveBtn"->node.isDisable = !tableViewItemIsSelected
                         "updateBtn"->node.isDisable = tableViewItemIsSelected
                         "deleteBtn"->node.isDisable = tableViewItemIsSelected
                     }
                 }
             }


         }
     }
}
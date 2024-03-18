package com.sonderben.trust.controller

import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.customView.RDButton
import com.sonderben.trust.db.dao.RoleDao
import com.sonderben.trust.hide
import com.sonderben.trust.model.Role
import com.sonderben.trust.viewUtil.ViewUtil
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.transformation.FilteredList

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class RoleController : Initializable, BaseController(), EventHandler<MouseEvent> {
    lateinit var mainPane: VBox
    private var roleSelectedOrToSave: Role? = null
    override fun initialize(location: URL?, resources: ResourceBundle?) {

        editMenuItem()
        MainController.hideBottomBar(false) {
            hideBottomPanelOnMouseClicked()
        }

        val listScreen = ScreenEnum.values().toList()
        gridPaneScreen.children.clear()


        var index = 0
        for (row in 0 until (listScreen.size / 4 + 1)) { // row
            for (col in 0 until 4) {// col
                if (index + 1 <= listScreen.size) {
                    val radioButton = RDButton(listScreen[index])
                    radioButton.onMouseClicked = this
                    gridPaneScreen.children.add(radioButton)


                    GridPane.setColumnIndex(radioButton, col)
                    GridPane.setRowIndex(radioButton, row)
                    index += 1
                } else {
                    break
                }
            }
        }
        //}
        nameCol.setCellValueFactory { data -> SimpleStringProperty(data.value.name) }
        screensCol.setCellValueFactory { d ->
            SimpleStringProperty(d.value.screens.joinToString(", ") {
                resources?.getString(
                    it.name.lowercase()
                ) ?: "not translate"
            })
        }

        tableView.items = RoleDao.roles


        tableView.selectionModel.selectedIndices.addListener( ListChangeListener { change->
            if ( change.list.isEmpty() ){
                enableActionButton(mainPane,true)
            }
            else{
                enableActionButton(mainPane,false)
            }
        } )


        tableView.selectionModel.selectionMode = SelectionMode.SINGLE
        tableView.selectionModel.selectedItemProperty().addListener(object : ChangeListener<Role> {
            override fun changed(observable: ObservableValue<out Role>?, oldValue: Role?, newValue: Role?) {
                if (newValue != null) {
                    clearRDButtons()

                    if (!bottomPanelVBOx.isVisible) {
                        bottomPanelVBOx.hide()
                    }

                    roleSelectedOrToSave = newValue
                    nameTf.text = newValue.name

                    for (screen in newValue.screens) {
                        gridPaneScreen.children.filtered {
                            (it as RDButton).name == ScreenEnum.valueOf( screen.name )
                        }.forEach {
                            (it as RDButton).select()
                        }
                    }
                }
            }
        })

    }

    override fun handle(event: MouseEvent?) {
        if (event != null) {
            if (event.source is RDButton) {
                if (event.eventType.equals(MouseEvent.MOUSE_CLICKED)) {
                    //val rdButton = event.source as RDButton
                    if (roleSelectedOrToSave == null) {
                        roleSelectedOrToSave
                    }
                }
            }

        }
    }

    private fun clearRDButtons() {
        gridPaneScreen.children.forEach {
            val rdButton = it as RDButton
            rdButton.clear()
        }

    }

    @FXML
    private lateinit var nameCol: TableColumn<Role, String>

    @FXML
    private lateinit var nameRb: RadioButton

    @FXML
    private lateinit var nameTf: TextField

    @FXML
    private lateinit var screensCol: TableColumn<Role, String>

    @FXML
    private lateinit var tableView: TableView<Role>

    @FXML
    private lateinit var gridPaneScreen: GridPane

    @FXML
    fun onClearRole() {
        clearAll()
    }

    @FXML
    private lateinit var bottomPanelVBOx: VBox

    private fun clearAll() {
        clearRDButtons()
        clear(mainPane)
        roleSelectedOrToSave = null

    }

    @FXML
    fun onSaveRole() {


        val rdButtonsChecked: FilteredList<Node> = gridPaneScreen.children.filtered { (it as RDButton).isChecked }

        RoleDao.save( Role(nameTf.text, rdButtonsChecked.map {  (it as RDButton).name  }.toMutableList()) ).subscribe({
            clearRDButtons()
        }, {th-> println("error: $th") })


    }

    @FXML
    fun onUpdateRole() {



        roleSelectedOrToSave!!.apply {
            name = nameTf.text
        }


        roleSelectedOrToSave?.let {
            RoleDao.update(it)
                .subscribe({ clear(mainPane) },{th-> println(th.message) })
        }
    }

    @FXML
    fun onDeleteRole() {
        roleSelectedOrToSave?.let {
            if (!it.name.equals("Admin")) {
                RoleDao.delete(it.id)
                    .subscribe({ clearAll() }, {})

            } else
                ViewUtil.createAlert(Alert.AlertType.WARNING, "Delete role", "Can not delete main role.").showAndWait()
        }

    }

    @FXML
    fun hideBottomPanelOnMouseClicked() {
        bottomPanelVBOx.hide()
    }

    override fun onDestroy() {

    }

    private fun editMenuItem() {
        MainController.editMenu?.items?.clear()


        val saveMenuItem = MenuItem("Save")
        saveMenuItem.setOnAction {
            onSaveRole()
        }
        val updateMenuItems = MenuItem("Update")
        updateMenuItems.setOnAction {
            onUpdateRole()
        }

        val deleteMenuItems = MenuItem("Delete")
        deleteMenuItems.setOnAction {
            onDeleteRole()
        }
        val clearMenuItems = MenuItem("Clear")
        clearMenuItems.setOnAction {
            clearAll()
        }


        MainController.editMenu?.items?.addAll(saveMenuItem, updateMenuItems, deleteMenuItems,clearMenuItems)

    }
}
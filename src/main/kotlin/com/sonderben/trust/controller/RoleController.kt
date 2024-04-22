package com.sonderben.trust.controller

import com.sonderben.trust.Context
import com.sonderben.trust.Util
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.customView.RDButton
import com.sonderben.trust.changeVisibility
import com.sonderben.trust.db.service.RoleService
import com.sonderben.trust.equalAtLeastOne
import com.sonderben.trust.viewUtil.ViewUtil
import entity.EmployeeEntity
import entity.Role
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
        disableQueryControlButton()
        editMenuItem()
        MainController.hideBottomBar(false) {
            hideBottomPanelOnMouseClicked()
        }

        val listScreen = Context.currentEmployee.get().role.screens

        gridPaneScreen.children.clear()


        var index = 0
        for (row in 0 until 4) { // row
            for (col in 0 until 4) {// col
                if (index >= listScreen.size   ){
                    break
                }
                    val radioButton = RDButton(listScreen[index])
                    radioButton.onMouseClicked = this
                    gridPaneScreen.children.add(radioButton)


                    GridPane.setColumnIndex(radioButton, col)
                    GridPane.setRowIndex(radioButton, row)
                index += 1

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

        tableView.items = RoleService.getInstance().entities

        if (Context.currentEmployee.get().isAdmin){
            val role = Context.currentEmployee.get().role
            if ( !tableView.items.contains( role ) ){
                tableView.items.add( role )

                if ( tableView.items.size > 1 )
                    Collections.swap( tableView.items,tableView.items.size-1,0 )
            }
        }



        tableView.selectionModel.selectedIndices.addListener(ListChangeListener { change ->
            if (change.list.isEmpty()) {
                enableActionButton(mainPane, true)
            } else {
                enableActionButton(mainPane, false)
            }
        })


        tableView.selectionModel.selectionMode = SelectionMode.SINGLE
        tableView.selectionModel.selectedItemProperty().addListener(object : ChangeListener<Role> {
            override fun changed(observable: ObservableValue<out Role>?, oldValue: Role?, newValue: Role?) {
                if (newValue != null) {
                    roleSelectedOrToSave = newValue
                    clearRDButtons()

                    if (!bottomPanelVBOx.isVisible) {
                        bottomPanelVBOx.changeVisibility()
                    }


                    nameTf.text = newValue.name

                    for (screen in newValue.screens) {
                        gridPaneScreen.children.filtered {
                            (it as RDButton).screen == ScreenEnum.valueOf(screen.name)
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


        if (validateRole()) {
            if ( !nameTf.text.equals("admin",true) && !nameTf.text.equals("seller",true) ){
                val rdButtonsChecked: FilteredList<Node> = gridPaneScreen.children.filtered { (it as RDButton).isSelected }

                RoleService.getInstance().save(Role(nameTf.text, rdButtonsChecked.map { (it as RDButton).screen }.toMutableList())).subscribe({
                    clearRDButtons()
                }, { th -> println("error: $th") })
            }else{
                ViewUtil.customAlert(ViewUtil.WARNING,"Admin or seller are reserved words, you can not use them.").show()
            }
        }


    }

    @FXML
    fun onUpdateRole() {
        println("validate: "+ validateRole(true))
        if ( validateRole(true) ) {
            val rdButtonsChecked: FilteredList<Node> = gridPaneScreen.children.filtered { (it as RDButton).isSelected }


            roleSelectedOrToSave!!.apply {
                name = nameTf.text
                screens = rdButtonsChecked.map { (it as RDButton).screen }.toMutableList()
            }
            RoleService.getInstance().update(roleSelectedOrToSave!!)
                .subscribe({ clear(mainPane) }, { th -> println(th.message) })
        }

    }

    private fun validateRole(validateId: Boolean = false): Boolean {

        if (validateId) {
            if ( roleSelectedOrToSave?.id == null && !roleSelectedOrToSave?.name.equals("admin",true)) {

                ViewUtil.customAlert(ViewUtil.WARNING, "please first select a role.").show()
                return false
            } else if (roleSelectedOrToSave!!.name.trim().equalAtLeastOne("admin", "seller", ignoreCase = true)) {

                ViewUtil.customAlert(ViewUtil.INFO, "Role \"Admin\" and \"Seller\" can't be update or delete.").show()
                return false
            }

        }
        if (Util.areBlank(nameTf) || gridPaneScreen.children.filtered { (it as RDButton).isSelected }.isEmpty()) {
            ViewUtil.customAlert(
                ViewUtil.WARNING,
                "Please select at least one screen authorization and fill out the name field."
            ).show()

            return false
        }

        return true
    }

    @FXML
    fun onDeleteRole() {
        if (validateRole(true)) {

            RoleService.getInstance().delete(roleSelectedOrToSave!!.id)
                .subscribe({ clearAll() }, { th -> println(th.message) })

        }

    }

    @FXML
    fun hideBottomPanelOnMouseClicked() {
        bottomPanelVBOx.changeVisibility()
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


        MainController.editMenu?.items?.addAll(saveMenuItem, updateMenuItems, deleteMenuItems, clearMenuItems)

    }
}
package com.sonderben.trust.controller

import SingletonView
import com.sonderben.trust.Context
import com.sonderben.trust.HelloApplication
import com.sonderben.trust.db.service.EmployeeService
import com.sonderben.trust.db.service.RoleService
import com.sonderben.trust.viewUtil.ViewUtil
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.util.Duration
import java.net.URL
import java.util.*


class MainController : Initializable {
    private fun selecttAppropriateView(idNode: String) {
        val selected = vboxLateral.children.filter { it.id.lowercase() == idNode }
        if (selected.isNotEmpty())
            onClickLateralButton(selected[0])
    }

    override fun initialize(location: URL?, resources: ResourceBundle) {

        //HelloApplication.primary.isResizable = true
        closeSession
        val toolTypeCloseSession = Tooltip("Close ${Context.currentEmployee.get().userName} session")
        val tooltipHideLeftPanel = Tooltip("Close or Open navigation panel")
        Tooltip.install(hideLeftPanel, tooltipHideLeftPanel)
        toolTypeCloseSession.hideDelay = Duration(5000.0)
        Tooltip.install(closeSession, toolTypeCloseSession)
        resourceBundle = resources
        hideLeftPanelOnMouseClicked()

        next = nextPage
        forward = forwardPage

        editMenu = menuEdit
        bottomBarMenuItem = bottomBar

        menuLanguage.items.forEach { menutem ->

            menutem.setOnAction {
                when (menutem.id) {

                    "fr" -> {
                        Context.writeJson("language", "fr")
                        println("id ${menutem.id}")
                    }

                    "es" -> {
                        Context.writeJson("language", "es")
                        println("id ${menutem.id}")
                    }

                    "en" -> {
                        Context.writeJson("language", "en")
                        println("id ${menutem.id}")
                    }

                    else -> {
                        println("id ${menutem.id}")
                    }
                }
            }
        }





        screensByRole()
        if (vboxLateral.children.size == 1) {
            hideLeftPanel.isVisible = false
            borderpane.left = null
            onClickLateralButton(vboxLateral.children[0])
        }
        if (vboxLateral.children.size > 0) {
            when (vboxLateral.children[0].id) {
                "sale" -> changeView("view/sale.fxml", "sale")
                "product" -> changeView("view/product.fxml", "product")
                "employee" -> changeView("view/employee.fxml", "employee")
                "role" -> changeView("view/role.fxml", "role")
                "enterprise" -> changeView("view/configuration.fxml", "enterprise")
                "queries" -> changeView("view/queries/queries.fxml", "queries")
                "customer_service" -> changeView("view/customerService.fxml", "customer_services")
            }

            onClickLateralButton(vboxLateral.children[0])
            onSelect()
        }

        for (items in vboxLateral.children) {
            val r = (items as HBox).children[1] as Label

            val menuItem = MenuItem(r.text)
            menuItem.id = "${items.id}_"



            when (menuItem.id.lowercase()) {
                "sale_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/sale.fxml", "sale")
                        selecttAppropriateView("sale")
                    }
                }

                "product_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/product.fxml", "product")
                        selecttAppropriateView("product")
                    }
                }

                "employee_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/employee.fxml", "employee")
                        selecttAppropriateView("employee")
                    }
                }

                "enterprise_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/configuration.fxml", "enterprise")
                        selecttAppropriateView("configuration")
                    }
                }

                "role_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/role.fxml", "role")
                        selecttAppropriateView("role")
                    }
                }

                "queries_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/queries/queries.fxml", "queries")
                        selecttAppropriateView("queries")
                    }
                }

                "customer_service_" -> {
                    menuItem.accelerator = KeyCodeCombination(KeyCode.DIGIT7, KeyCombination.CONTROL_DOWN)
                    menuItem.setOnAction {
                        changeView("view/customerService.fxml", "customer_services")
                        selecttAppropriateView("customer_service")
                    }
                }
            }

            menuView.items.addAll(
                menuItem
            )
        }


    }


    lateinit var menuLanguage: Menu
    lateinit var hideLeftPanel: ImageView
    lateinit var closeSession: ImageView
    lateinit var windowTitle: Label
    private lateinit var resourceBundle: ResourceBundle
    lateinit var bottomBar: MenuItem


    @FXML
    private lateinit var vboxLateral: VBox

    @FXML
    private lateinit var stackPane: StackPane

    @FXML
    private lateinit var borderpane: BorderPane

    @FXML
    private lateinit var menuBar: MenuBar

    @FXML
    private lateinit var topBarHBox: HBox

    @FXML
    private lateinit var menuEdit: Menu

    @FXML
    private lateinit var nextPage: ImageView

    @FXML
    private lateinit var forwardPage: ImageView

    @FXML
    private lateinit var menuView: Menu


    companion object {
        var next: ImageView? = null
        var forward: ImageView? = null
        var editMenu: Menu? = null
        var bottomBarMenuItem: MenuItem? = null

        fun hideBottomBar(hide: Boolean, onAction: (() -> Unit)? = null) {
            if (bottomBarMenuItem != null) {
                bottomBarMenuItem!!.isDisable = hide

                if (!hide)
                    bottomBarMenuItem!!.setOnAction {
                        onAction?.invoke()
                    }
            }

        }
    }


    private fun onSelect() {
        vboxLateral.children.forEachIndexed { _, it ->
            it.setOnMouseClicked { event ->
                if (event != null) {
                    onClickLateralButton(event.source as Node)
                    when (it.id.lowercase()) {
                        "sale" -> changeView("view/sale.fxml", "sale")
                        "product" -> changeView("view/product.fxml", "product")
                        "employee" -> changeView("view/employee.fxml", "employee")
                        "enterprise" -> changeView("view/configuration.fxml", "enterprise")
                        "inventory" -> changeView("view/inventory.fxml", "")
                        "role" -> changeView("view/role.fxml", "role")
                        "queries" -> changeView("view/queries/queries.fxml", "queries")
                        "customer_service" -> changeView("view/customerService.fxml", "customer_services")
                    }
                }
            }
        }
    }

    private fun changeView(relativeUrl: String, titleI18n: String) {
        val a = SingletonView.get(relativeUrl)
        if (a != null && !stackPane.children.contains(a)) {
            stackPane.children.clear()
            stackPane.children.add(a)
        }
        windowTitle.text = resourceBundle.getString(titleI18n)
        //a?.toFront()
    }

    private fun screensByRole() {
        val screensIdEmployee: List<String> =
            Context.currentEmployee.value.role.screens.map { screen -> screen.name.lowercase() }
        vboxLateral.children.removeIf { !screensIdEmployee.contains(it.id.lowercase()) }
    }


    private fun onClickLateralButton(node: Node) {
        vboxLateral.children.forEach {
            if (node === it) {
                it.style = "-fx-background-color: #FFFFFF35"
            } else {
                it.style = ""
            }
        }
    }

    private var xOffset = 0.0
    private var yOffset = 0.0
    fun topBarHBoxOnMousePressed(event: MouseEvent) {
        xOffset = event.screenX - HelloApplication.primary.x
        yOffset = event.screenY - HelloApplication.primary.y
    }

    fun topBarHBoxOnMouseDragged(event: MouseEvent) {
        HelloApplication.primary.x = event.screenX - xOffset
        HelloApplication.primary.y = event.screenY - yOffset
    }


    fun hideLeftPanelOnMouseClicked() {

        if (borderpane.left.isVisible) {
            borderpane.left.isVisible = false
            borderpane.left.managedProperty().set(false)
        } else {
            borderpane.left.isVisible = true
            borderpane.left.managedProperty().set(true)
        }

    }

    @FXML
    fun lateralBarOnAction() {

        if (borderpane.left != null) {

            if (borderpane.left.isVisible) {
                borderpane.left.isVisible = false
                borderpane.left.managedProperty().set(false)
            } else {
                borderpane.left.isVisible = true
                borderpane.left.managedProperty().set(true)
            }


        }

    }

    @FXML
    fun disconnectOnMouseClicked() {
        Context.currentEmployee.value = null
        RoleService.clearInstance()
        EmployeeService.clearInstance()


        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("login.fxml"), resourceBundle)
        val scene =
            Scene(fxmlLoader.load(), HelloApplication.primary.scene.width, HelloApplication.primary.scene.height)
        HelloApplication.primary.scene = scene
    }

    fun editCredentialMenutitemOnAction() {

        val dialog = EditCredentialDialog(HelloApplication.primary.width * 0.60)
        dialog.initOwner(HelloApplication.primary)
        dialog.showAndWait()

    }

    fun technologyOnAction(actionEvent: ActionEvent) {
        //changeView("view/technologies.fxml","technologies")
        ViewUtil.technology().show()
    }

    fun signOutOnAction() {
        disconnectOnMouseClicked()
    }

}
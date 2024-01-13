package com.sonderben.trust

import java.text.NumberFormat
import java.util.*

class Util {

}
/*
package com.sonderben.trust.customView

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.Line


class RolePane(screens:List<String>,actions:List<String>) : Pane() {
    private val nameTitle: Label
    private val screenTitle: Label
    private val actionTitle: Label

    private val loginTextField: Label
    private val inventoryTextField: Label
    private val userTextField: Label
    private val roleTextField: Label
    private val readTextField: Label
    private val addTextField: Label
    private val updateTextField: Label
    private val deleteTextField: Label
    private val nameTextField: TextField
    private val eCircle: Circle
    private val eLogin: Circle
    private val eInventory: Circle
    private val eUser: Circle
    private val eRole: Circle
    private val sLogin: Circle
    private val sInventory: Circle
    private val sUser: Circle
    private val sRole: Circle
    private val sRead: Circle
    private val sAdd: Circle
    private val sUpdate: Circle
    private val sDelete: Circle
    private var lineNameToScreen:Line = Line()


    init {
        nameTitle = Label("Name")
        screenTitle = Label("Screen")
        actionTitle = Label("Action")
        loginTextField = Label("Login".fill())
        inventoryTextField = Label("Inventory".fill())
        userTextField = Label("User".fill())
        roleTextField = Label("Role".fill())
        readTextField = Label("Read".fill())
        addTextField = Label("Add".fill())
        updateTextField = Label("Update".fill())
        deleteTextField = Label("Delete".fill())
        nameTextField = TextField()
        nameTextField.promptText = "Name"
        eCircle = Circle(8.0)
        eLogin = Circle(8.0)
        eInventory = Circle(8.0)
        eUser = Circle(8.0)
        eRole = Circle(8.0)
        sLogin = Circle(8.0)
        sInventory = Circle(8.0)
        sUser = Circle(8.0)
        sRole = Circle(8.0)
        sRead = Circle(8.0)
        sAdd = Circle(8.0)
        sUpdate = Circle(8.0)
        sDelete = Circle(8.0)

        lineNameToScreen.startXProperty().bind( eCircle.centerXProperty() )
        lineNameToScreen.startYProperty().bind( eCircle.centerYProperty() )
        lineNameToScreen.visibleProperty().bind( eCircle.focusedProperty() )

        val a = BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)
        this.background = Background( a )

        this.maxWidth = Double.MAX_VALUE
        this.maxHeight = Double.MAX_VALUE
        add()

        /*cubicCurve.startX = eCircle.centerX
        cubicCurve.endY = eCircle.centerY*/


        eCircle.setOnMouseDragged(object :EventHandler<MouseEvent>{
            override fun handle(event: MouseEvent?) {
                if (event != null) {
                    eCircle.requestFocus()
                    lineNameToScreen.endX = event.x
                    lineNameToScreen.endY = event.y
                    //println( "setOnMouseDragged" )
                }
            }
        })
        eCircle.setOnMouseReleased (object :EventHandler<MouseEvent>{
            override fun handle(event: MouseEvent?) {
                nameTitle.requestFocus()
            }
        })
    }

    override fun resize(width: Double, height: Double) {
        super.resize(width, height)
        resizePane(height,width)

    }
    private fun resizePane(height: Double, width: Double) {
        val centerX = width / 2 - screenTitle.width / 2
        val startX = 10.0
        val endX = width - (screenTitle.width) - 10
        nameTitle.layoutX = startX
        nameTitle.layoutY = 10.0


////////////////

        screenTitle.layoutX = centerX
        screenTitle.layoutY = 10.0
        placeScreen(sLogin,eLogin, loginTextField,60.0)
        placeScreen(sInventory,eInventory, inventoryTextField,90.0)
        placeScreen(sUser,eUser, userTextField,120.0)
        placeScreen(sRole,eRole, roleTextField,150.0)


        actionTitle.layoutX = endX
        actionTitle.layoutY = 10.0

        placeAction(sRead,readTextField,60.0)
        placeAction(sAdd,addTextField,90.0)
        placeAction(sUpdate,updateTextField,120.0)
        placeAction(sDelete,deleteTextField,150.0)

        nameTextField.layoutX = startX
        nameTextField.layoutY = height/2

        eCircle.centerX = startX + nameTextField.width + 20
        eCircle.centerY = height/2 + eCircle.radius *2
    }

    private fun placeScreen(sCLogin:Circle, eCLogin:Circle, label:Label, layoutY:Double) {
        label.layoutX = (width / 2)-label.width / 2
        label.layoutY = layoutY-(label.height/2)

        sCLogin.centerX = label.layoutX-15
        sCLogin.centerY = layoutY

        eCLogin.centerX = label.layoutX+15+label.width
        eCLogin.centerY = layoutY
    }

    private fun placeAction(sCLogin:Circle, label:Label, layoutY:Double) {
        val endX = width - (label.width) - 10

        label.layoutX = width - (label.width) - 10
        label.layoutY = layoutY-(label.height/2)

        sCLogin.centerX = label.layoutX-15
        sCLogin.centerY = layoutY


    }

    private fun add() {
        children.addAll(
            lineNameToScreen,
            eCircle, eLogin, eInventory,
            eUser, eRole, sLogin, sInventory, sUser, sRole,
            sRead, sAdd, sUpdate, sDelete, nameTextField,
            nameTitle, screenTitle, actionTitle,
            loginTextField, inventoryTextField,
            userTextField, roleTextField,
            readTextField, addTextField, updateTextField, deleteTextField
        )
    }
    fun String.fill():String{
        var  temp = this
        if (temp.length<12){
            while ( temp.length < 12){
                temp+=" "
            }
        }
        println("size: "+temp.length)
        return temp;
    }
}

 */
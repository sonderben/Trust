package com.sonderben.trust.customView

import com.sonderben.trust.constant.Action
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.model.Role
import com.sonderben.trust.model.Screen
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import java.lang.Exception
import java.lang.RuntimeException


class RolePane(screens: List<String>, actions: List<String>) : Pane(), EventHandler<MouseEvent> {
    private val paddingX = 10.0
    private val nameTitle: Label
    private val screenTitle: Label
    private val actionTitle: Label
    private var role = SimpleObjectProperty<Role>()

    private val nameTextField: TextField = TextField()
    private val namePoint = Point("name", type = Point.Type.MUL_OUT_1)

    private val screensLabel = mutableListOf<Label>()
    private val startScreensPoint = mutableListOf<Point>()
    private val endScreensPoint = mutableListOf<Point>()
    private val fakeLinkPoint = LinkPoint()

    private val actionsLabel = mutableListOf<Label>()
    private val startActionsPoint = mutableListOf<Point>()
    private var roleTemp = Role("", mutableListOf())


    init {




        this.style = "-fx-background-color:#032d3b;"
        this.prefHeight = 500.0



        children.add(fakeLinkPoint)
        nameTitle = Label("Name")
        screenTitle = Label("Screen")
        actionTitle = Label("Action")


        placeTitlesLabel()

        nameTextField.promptText = "require"


        for (screen in screens) {
            val label = Label(screen)
            val sPoint = Point(screen, type = Point.Type.IN_1)
            sPoint.onMouseClicked = this

            val ePoint = Point(screen, type = Point.Type.MUL_OUT_2)
            ePoint.onMouseClicked = this
            screensLabel.add(label)
            startScreensPoint.add(sPoint)
            endScreensPoint.add(ePoint)

        }

        for (action in actions) {
            actionsLabel.add(Label(action.fill()))
            val point = Point(name = action, type = Point.Type.IN_2)
            point.onMouseClicked = this
            startActionsPoint.add( point )
        }

        add()
        namePoint.onMouseClicked = this
        this.onMouseMoved = this




        role.addListener { _, _, newRole ->

            if (newRole != null) {

                println( "change happen" )
                this@RolePane.children.removeIf { it is LinkPoint }
                nameTextField.text = newRole.name

                val screenList: List<Screen> = newRole.screens
                for (screen in screenList) {
                    val point: Point = getPointByName(startScreensPoint, screen.screen.name)
                    val line = LinkPoint()
                    line.startXProperty().bind(namePoint.layoutXProperty())
                    line.startYProperty().bind(namePoint.layoutYProperty())

                    line.endXProperty().bind(point.layoutXProperty())
                    line.endYProperty().bind(point.layoutYProperty())
                    this@RolePane.children.add(line)


                    val actionList = screen.actions
                    for (action in actionList) {
                        val endPointScreen: Point = getPointByName(endScreensPoint, screen.screen.name)
                        val startPointAction: Point = getPointByName(startActionsPoint, action.name)
                        val line2 = LinkPoint()

                        line2.startXProperty().bind(endPointScreen.layoutXProperty())
                        line2.startYProperty().bind(endPointScreen.layoutYProperty())

                        line2.endXProperty().bind(startPointAction.layoutXProperty())
                        line2.endYProperty().bind(startPointAction.layoutYProperty())
                        this@RolePane.children.add(line2)

                    }
                }


            }
        }



        val rol1 = Role("Sale", mutableListOf(  Screen(ScreenEnum.SALE, mutableListOf( Action.ADD,Action.DELETE,Action.READ,Action.UPDATE ))  ) )


        //val ps:List<Role> = roleCrud.findAll( Role::class.java )

       // println( ps)

       // role.set( ps[0] )





    }



    private fun placeScreenPointLabel() {

        var stepY = 60.0
        val tempLabel: Label = screensLabel.maxBy { it.text.length }

        for (i in 0 until screensLabel.size) {
            startScreensPoint[i].layoutX = width / 2
            startScreensPoint[i].layoutY = stepY + (startScreensPoint[i].getRadius())

            val temp = width / 2 + startScreensPoint[i].getRadius() + paddingX
            screensLabel[i].layoutX = temp
            screensLabel[i].layoutY = stepY

            //
            endScreensPoint[i].layoutX = temp + tempLabel.width + paddingX + paddingX
            endScreensPoint[i].layoutY = stepY + (endScreensPoint[i].getRadius())
            //

            stepY += height/5//40.0
        }
    }

    private fun placeActionPointLabel() {

        var stepY = 60.0
        val tempLabel: Label = screensLabel.maxBy { it.text.length }

        for (i in 0 until actionsLabel.size) {

            actionsLabel[i].layoutX = width - tempLabel.width - paddingX
            actionsLabel[i].layoutY = stepY

            startActionsPoint[i].layoutX = actionsLabel[i].layoutX - paddingX - startActionsPoint[i].getRadius()
            startActionsPoint[i].layoutY = stepY + (startActionsPoint[i].getRadius())


            stepY += height/4//40.0
        }
    }

    private fun getPointByName(points: List<Point>, name: String): Point {
        val x = points.filter { it.getName().lowercase() == name.lowercase() }

        if (x.isNotEmpty())
            return x[0]
        throw RuntimeException(" no encuentre ese point")
    }


    private fun placeTitlesLabel() {
        nameTitle.layoutX = paddingX
        nameTitle.layoutY = paddingX

        screenTitle.layoutX = width / 2
        screenTitle.layoutY = paddingX

        actionTitle.layoutX = width - actionTitle.width - paddingX
        actionTitle.layoutY = paddingX
    }

    override fun resize(width: Double, height: Double) {
        super.resize(width, height)
        placeTitlesLabel()
        placeScreenPointLabel()

        placeNameTextFieldPoint()
        placeActionPointLabel()

    }

    private fun placeNameTextFieldPoint() {
        nameTextField.layoutX = paddingX
        nameTextField.layoutY = height / 2

        namePoint.layoutX = nameTextField.layoutX + nameTextField.width + paddingX + namePoint.getRadius()
        namePoint.layoutY = height / 2 + namePoint.getRadius() * 2
    }


    private fun add() {
        children.addAll(
            nameTitle, screenTitle, actionTitle, nameTextField, namePoint
        )
        children.addAll(screensLabel)
        children.addAll(actionsLabel)
        children.addAll(startActionsPoint)
        children.addAll(endScreensPoint)
        children.addAll(startScreensPoint)
    }

    private fun String.fill(): String {
        var temp = this
        if (temp.length < 12) {
            while (temp.length < 12) {
                temp += " "
            }
        }
        return temp;
    }

    override fun handle(event: MouseEvent?) {

        if (event != null) {

            if (event.eventType == MouseEvent.MOUSE_CLICKED) {

                if (event.source is Point) {
                    val point = event.source as Point

                    if ( !this@RolePane.children.contains( fakeLinkPoint ) )
                        this@RolePane.children.add( fakeLinkPoint )

                    if (point.getType()?.equals(Point.Type.MUL_OUT_1) == true) {
                        fakeLinkPoint.startXProperty().bind(point.layoutXProperty())
                        fakeLinkPoint.startYProperty().bind(point.layoutYProperty())
                        fakeLinkPoint.from = point.getName()
                        this@RolePane.onMouseMoved = this@RolePane
                    }
                    if (point.getType()?.equals(Point.Type.IN_1) == true) {
                        fakeLinkPoint.startXProperty().unbind()
                        fakeLinkPoint.startYProperty().unbind()

                        fakeLinkPoint.to = point.getName()

                        if (fakeLinkPoint.from.equals("name",true)  ){
                            val screenEnum:ScreenEnum = stringToScreenEnum( fakeLinkPoint.to )
                            roleTemp.screens.add(Screen(screenEnum, mutableListOf()))
                            val tmp = Role( roleTemp.name.repeat(2)+"b",roleTemp.screens )
                            role.set(null)
                            role.set(tmp)
                        }
                        this@RolePane.onMouseMoved = null
                    }
                    if (point.getType()?.equals( Point.Type.MUL_OUT_2 ) == true){
                        fakeLinkPoint.startXProperty().bind(point.layoutXProperty())
                        fakeLinkPoint.startYProperty().bind(point.layoutYProperty())
                        fakeLinkPoint.from = point.getName()
                        this@RolePane.onMouseMoved = this@RolePane
                    }
                    if (point.getType()?.equals( Point.Type.IN_2 ) == true){
                        fakeLinkPoint.startXProperty().unbind()
                        fakeLinkPoint.startYProperty().unbind()

                        fakeLinkPoint.to = point.getName()

                        if (!fakeLinkPoint.from.equals("name",true)  ){
                            val screenEnum:ScreenEnum = stringToScreenEnum( "Login" )//es el point antes(Screen)

                            val actionEnum:Action = stringToActionEnum( fakeLinkPoint.to )
                            val tempScreens:Screen = roleTemp.screens.filter { it.screen == screenEnum }[0]

                            tempScreens.actions.add( actionEnum )

                            role.set( null )

                            role.set( Role( roleTemp.name,roleTemp.screens ) )



                        }
                        this@RolePane.onMouseMoved = null
                    }
                }


            } else if (event.eventType == MouseEvent.MOUSE_MOVED) {
                fakeLinkPoint.endX = event.x-3
                fakeLinkPoint.endY = event.y-3
            }

        }
    }

    private fun stringToScreenEnum(from: String): ScreenEnum {
        val screenEnums:Array<ScreenEnum> = ScreenEnum.values()
        val found = screenEnums.filter{ it.name.equals( from,ignoreCase = true ) }

        if ( found.isNotEmpty()){
            return found[0]
        }
        throw Exception("No encontre ese Screen Enum")
    }

    private fun stringToActionEnum(from: String): Action {
        val actionEnums:Array<Action> = Action.values()
        val found = actionEnums.filter{ it.name.equals( from,ignoreCase = true ) }

        if ( found.isNotEmpty()){
            return found[0]
        }
        throw Exception("No encontre ese Action Enum")
    }

}

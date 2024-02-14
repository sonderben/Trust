package com.sonderben.trust.customView

import com.sonderben.trust.constant.ScreenEnum
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.StrokeType

class RDButton( var name:ScreenEnum,var isChecked:Boolean = false):Pane(),EventHandler<MouseEvent> {
    private var radius = 8.0
    private var outerCircle: Circle = Circle(radius)
    private var innerCircle: Circle
    private val label = Label(name.name.toLowerCase().replace("_"," ").replaceFirstChar { it.uppercase() })
    init {
        label.style = "-fx-text-fill: white;"

        //this.style = "-fx-background-color:black"
        this.prefHeight = radius*2
        this.prefWidth = radius*2
        outerCircle.stroke= Color.WHITE
        outerCircle.strokeWidth = 2.0
        outerCircle.fill = Color.WHITE//Color.TRANSPARENT
        outerCircle.strokeType = StrokeType.OUTSIDE

        innerCircle = Circle(4.0)
        innerCircle.fill = Color.RED

        innerCircle.isVisible = isChecked

        label.layoutX = outerCircle.layoutX+outerCircle.radius*2
        label.layoutY = 0.0

        outerCircle.layoutY = radius
        innerCircle.layoutY = radius

        children.addAll(outerCircle,innerCircle,label)

        outerCircle.onMouseClicked = this
        innerCircle.onMouseClicked = this


    }

    fun clear(){
        innerCircle.isVisible = false
    }

    fun select(){
        innerCircle.isVisible = true
    }

    override fun handle(event: MouseEvent?) {
        if (event != null) {
            if (event.eventType == MouseEvent.MOUSE_CLICKED){
                isChecked = !isChecked
                innerCircle.isVisible = isChecked
            }
        }
    }


}
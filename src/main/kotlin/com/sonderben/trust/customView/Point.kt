package com.sonderben.trust.customView

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.StrokeType

class Point(private var name:String,private var radius:Double=8.0,private var type:Type?=null):Region() {
    private var outerCircle:Circle = Circle(radius)
    private var innerCircle:Circle

    public fun getName()=name
    init {
        outerCircle.stroke= Color.GREEN
        outerCircle.strokeWidth = 2.0
        outerCircle.fill = Color.TRANSPARENT
        outerCircle.strokeType = StrokeType.OUTSIDE

         innerCircle = Circle(radius-5)
        innerCircle.fill = Color.RED

        children.addAll(outerCircle,innerCircle)



        this.onMouseEntered = object :EventHandler<MouseEvent>{
            override fun handle(event: MouseEvent?) {
                innerCircle.opacity  = 0.3
                outerCircle.strokeWidth = 5.0
            }
        }
        this.onMouseExited = object :EventHandler<MouseEvent>{
            override fun handle(event: MouseEvent?) {
                innerCircle.opacity  = 1.0
                outerCircle.strokeWidth = 2.0
            }
        }

    }
    fun getRadius():Double{
        return radius;
    }
    fun getType() = type

    fun setType(type:Type?){
        this.type=type
    }

    fun getCenterX() = outerCircle.centerX
    fun getCenterY() = outerCircle.centerY

    fun getCenterXProperty() = outerCircle.centerXProperty()
    fun getCenterYProperty() = outerCircle.centerYProperty()


    enum class Type{
        MUL_OUT_1,MUL_OUT_2,IN_1,IN_2
    }


}
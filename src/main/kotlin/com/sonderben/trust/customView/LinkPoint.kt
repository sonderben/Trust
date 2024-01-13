package com.sonderben.trust.customView


import javafx.scene.paint.Color
import javafx.scene.shape.Line

class LinkPoint:Line() {

    public var from:String = ""

     var to:String = ""





    init {
        stroke = Color.BLACK
        strokeWidth = 3.0

        var currentColour = Color.BLACK


        this.setOnMouseClicked { this@LinkPoint.requestFocus() }

        this.setOnMouseEntered { stroke=Color.ORANGE }
        this.setOnMouseExited { stroke=currentColour }

        this.focusedProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                if (newValue) {
                    currentColour = Color.GREEN
                    stroke = currentColour
                    strokeWidth = 5.0

                } else {
                    currentColour = Color.BLACK
                    stroke = currentColour
                    strokeWidth = 3.0
                }
            }
        }
    }
}
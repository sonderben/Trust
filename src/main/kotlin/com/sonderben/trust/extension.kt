package com.sonderben.trust

import javafx.scene.layout.VBox

fun VBox.hide(){
    if (this.isVisible){
        this.isVisible = false
        this.managedProperty().set(false)
    }else{
        this.isVisible = true
        this.managedProperty().set(true)
    }
}
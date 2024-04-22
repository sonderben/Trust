package com.sonderben.trust.customView

import com.sonderben.trust.constant.Constant
import com.sonderben.trust.constant.ScreenEnum
import javafx.scene.control.RadioButton

class RDButton(var screen:ScreenEnum,  isChecked:Boolean = false): RadioButton( Constant.resource.getString(screen.name.lowercase()) ) {


    init {
        if (screen == ScreenEnum.ENTERPRISE){
            this.isDisable = true
        }
        isSelected = isChecked

    }

    fun clear(){
        isSelected = false
    }

    fun select(){
        isSelected  = true
    }

}
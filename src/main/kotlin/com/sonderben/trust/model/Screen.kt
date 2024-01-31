package com.sonderben.trust.model

import com.sonderben.trust.constant.Action
import com.sonderben.trust.constant.ScreenEnum
import entity.BaseEntity



data class Screen (

    var screen: ScreenEnum,

    var actions:MutableList<Action>): BaseEntity(){
    constructor() : this(ScreenEnum.LOGIN, mutableListOf())
}




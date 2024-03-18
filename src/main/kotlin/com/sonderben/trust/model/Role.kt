package com.sonderben.trust.model

import com.sonderben.trust.constant.ScreenEnum
import entity.BaseEntity



data class Role(var name: String,  var screens:MutableList<ScreenEnum>): BaseEntity() {
    constructor() : this("", mutableListOf())
}


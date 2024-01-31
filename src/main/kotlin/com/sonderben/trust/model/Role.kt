package com.sonderben.trust.model

import entity.BaseEntity



data class Role(var name: String,  var screens:MutableList<Screen>): BaseEntity() {
    constructor() : this("", mutableListOf())
}


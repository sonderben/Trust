package com.sonderben.trust.model

import com.sonderben.trust.constant.Action
import com.sonderben.trust.constant.ScreenEnum
import com.sonderben.trust.db.entity.BaseEntity
import jakarta.persistence.*

@Entity
data class Screen (
    @Enumerated(value = EnumType.STRING)
    var screen: ScreenEnum,
    @Enumerated @ElementCollection(targetClass = Action::class)
    var actions:MutableList<Action>):BaseEntity(){
    constructor() : this(ScreenEnum.LOGIN, mutableListOf())
}
package com.sonderben.trust.model

import entity.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
data class Role(var name: String, @OneToMany(cascade = [CascadeType.ALL]) var screens:MutableList<Screen>): BaseEntity() {
    constructor() : this("", mutableListOf())
}


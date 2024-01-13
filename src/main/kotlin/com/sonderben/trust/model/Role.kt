package com.sonderben.trust.model


data class Role(var name: String,var screens:MutableList<Screen>) {

/*
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (name != other.name) return false
        return screens == other.screens
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + screens.hashCode()
        return result
    }*/
}
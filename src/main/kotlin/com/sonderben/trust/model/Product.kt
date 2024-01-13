package com.sonderben.trust.model

import com.sonderben.trust.db.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty


@Entity
data class Product(
    var code: String,
    var description: String,
    var price: Double,
    var discount: Double,
    var itbis: Double,
    var quantity: Int
) : BaseEntity() {

    constructor() : this("", "", 0.0, 0.0, 0.0, 0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (description != other.description) return false
        if (code != other.code) return false
        if (price != other.price) return false
        if (discount != other.discount) return false
        if (itbis != other.itbis) return false
        return quantity == other.quantity
    }

    public fun total(): Double {
        val itbisPrice = itbis + price
        if (discount <= 0)
            return itbisPrice * quantity
        return (itbisPrice + (discount * itbisPrice)) * quantity
    }

    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + discount.hashCode()
        result = 31 * result + itbis.hashCode()
        result = 31 * result + quantity.hashCode()
        return result
    }

    override fun toString(): String {
        return "Product(description=$description, code=$code, price=$price, discount=$discount, itbis=$itbis, quantity=$quantity)"
    }


}
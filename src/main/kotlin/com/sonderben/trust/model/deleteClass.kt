package com.sonderben.trust.model

import jakarta.persistence.*

@Entity
@Table(name = "DELETEC")
data class deleteClass(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var genial: String,
    var dffcc: String
) {
    constructor() : this(null,"","") {}
    override fun toString(): String {
        return "deleteClass(id=$id, genial='$genial', dffcc='$dffcc')"
    }
}
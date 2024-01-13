package com.sonderben.trust.db.entity

import jakarta.persistence.*
import java.io.Serializable


@MappedSuperclass
abstract class BaseEntity(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) public var id:Long?=null):Serializable

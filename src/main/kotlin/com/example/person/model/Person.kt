package com.example.person.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class Person(

    @Id
    val _id: ObjectId = ObjectId(),
    var uid: String,
    var name: String,
    var address: Address)
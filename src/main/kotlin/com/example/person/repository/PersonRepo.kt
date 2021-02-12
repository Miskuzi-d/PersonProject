package com.example.person.repository

import com.example.person.model.Person
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface PersonRepo: MongoRepository<Person, ObjectId> {
    fun deleteByUid(uid: String)
    fun findByUid(uid: String): Person
}
package com.example.person.service

import com.example.person.dto.PersonDTO

interface PersonService {
    fun save(personDTO: PersonDTO): PersonDTO
    fun findAll(): List<PersonDTO?>
    fun deleteByUid(uid: String)
    fun updateOne(uid: String, personDTO: PersonDTO): PersonDTO
    fun findByUid(uid: String): PersonDTO
}
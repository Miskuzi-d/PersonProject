package com.example.person.controller

import com.example.person.dto.PersonDTO
import com.example.person.service.PersonService
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/person")
class PersonController(private val personService: PersonService) {

    @PostMapping("/post")
    fun save(@RequestBody personDTO: PersonDTO): ResponseEntity<PersonDTO> =
        try {
            ResponseEntity.ok(personService.save(personDTO))
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity.notFound().build()
        }

    @GetMapping("/get")
    fun findAll() = ResponseEntity.ok(personService.findAll())

    @GetMapping("/{uid}")
    fun findByUid(@PathVariable("uid") uid: String): ResponseEntity<PersonDTO> =
        try {
            ResponseEntity.ok(personService.findByUid(uid))
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity.notFound().build()
        }

    @DeleteMapping("/{uid}")
    fun delete(@PathVariable("uid") uid: String): ResponseEntity<Unit> =
        try {
            ResponseEntity.ok(personService.deleteByUid(uid))
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity.notFound().build()
        }

    @PostMapping("/{uid}")
    fun updateByUid(@PathVariable("uid") uid: String, @RequestBody personDTO: PersonDTO): ResponseEntity<PersonDTO?> =
        try {
            ResponseEntity.ok(personService.updateOne(uid, personDTO))
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity.notFound().build()
        }
}
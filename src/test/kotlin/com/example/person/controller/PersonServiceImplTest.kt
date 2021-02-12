package com.example.person.controller

import com.example.person.dto.PersonDTO
import com.example.person.model.Address
import com.example.person.model.Person
import com.example.person.repository.PersonRepo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.data.mongodb.port=28018"])
internal class PersonControllerTest {

    @Autowired
    private lateinit var personRepo: PersonRepo

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @LocalServerPort
    private var port: Int = 0

    private val defaultUid = UUID.randomUUID().toString()

    @BeforeEach
    private fun setUp() {
        saveOnePerson()
    }

    @AfterEach
    private fun cleaning() {
        personRepo.deleteAll()
    }

    private fun preparePersonDTO() = PersonDTO(
        uid = UUID.randomUUID().toString(),
        name = "qwe",
        city = "qwe",
        street = "qwe",
        houseNum = 7
    )

    private fun saveOnePerson() = personRepo.save(
        Person(
            uid = defaultUid,
            name = "test",
            address = Address(
                city = "test",
                street = "test",
                houseNum = 8
            )
        )
    )

    @Test
    fun createNewPerson(){
        personRepo.deleteAll()
        val personDTO = preparePersonDTO()
        val emptyResponse = restTemplate.exchange(
            "http://localhost:${port}/",
            HttpMethod.POST,
            HttpEntity(personDTO, ),
            ResponseEntity::class.java)

        assertEquals(404, emptyResponse.statusCodeValue)

        personDTO.uid = ""

        val personResponse = restTemplate.exchange(
            "http://localhost:${port}/",
            HttpMethod.POST,
            HttpEntity(personDTO),
            PersonDTO::class.java)

        assertEquals(200, personResponse.statusCodeValue)

        personDTO.uid = ""

        val updatePerson = personRepo.findAll()[0]
        assertEquals(personResponse.body?.uid, updatePerson.uid)
        assertEquals(personResponse.body?.name, updatePerson.name)
        assertEquals(personResponse.body?.city, updatePerson.address.city)
    }

    @Test
    fun deletePerson(){
        val emptyResponse = restTemplate.exchange(
            "http://localhost:${port}/${defaultUid}",
            HttpMethod.DELETE,
            HttpEntity(null,null),
            Unit::class.java)

        assertEquals(200, emptyResponse.statusCodeValue)

        Assertions.assertThrows(EmptyResultDataAccessException::class.java) { personRepo.findByUid(defaultUid) }
    }

    @Test
    fun findAllPersons(){
        val emptyResponse = restTemplate.exchange(
            "http://localhost:${port}/",
            HttpMethod.GET,
            HttpEntity(null,null),
            List::class.java
        )

        assertEquals(200, emptyResponse.statusCodeValue)
    }

    @Test
    fun updatePerson(){
        val personDTO = preparePersonDTO()
        val emptyResponse = restTemplate.exchange(
            "http://localhost:${port}/${defaultUid}",
            HttpMethod.POST,
            HttpEntity(personDTO),
            PersonDTO::class.java
        )

        assertEquals(404, emptyResponse.statusCodeValue)

        personDTO.uid = ""
        val personResponse = restTemplate.exchange(
            "http://localhost:${port}/${defaultUid}",
            HttpMethod.POST,
            HttpEntity(personDTO),
            PersonDTO::class.java
        )

        assertEquals(200, personResponse.statusCodeValue)

        val updatePerson = personRepo.findAll()[0]
        assertEquals(personResponse.body?.uid, updatePerson.uid)
        assertEquals(personResponse.body?.name, updatePerson.name)
        assertEquals(personResponse.body?.city, updatePerson.address.city)
        assertEquals(personResponse.body?.street, updatePerson.address.city)
        assertEquals(personResponse.body?.houseNum, updatePerson.address.houseNum)

    }

    @Test
    fun findByUid(){
        val emptyResponse = restTemplate.exchange(
            "http://localhost:${port}/${defaultUid}",
            HttpMethod.GET,
            HttpEntity(null, null),
            PersonDTO::class.java
        )

        assertEquals(200, emptyResponse.statusCodeValue)

        val notFoundPersonResponse = restTemplate.exchange(
            "http://localhost:${port}/1",
            HttpMethod.GET,
            HttpEntity(null, null),
            PersonDTO::class.java
        )

        assertEquals(404, notFoundPersonResponse.statusCodeValue)
    }
}
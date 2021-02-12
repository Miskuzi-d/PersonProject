package com.example.person.service

import com.example.person.dto.PersonDTO
import com.example.person.dto.mapper.StructureMapper
import com.example.person.model.Address
import com.example.person.model.Person
import com.example.person.repository.PersonRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PersonServiceImpl(private val personRepo: PersonRepo) : PersonService {


    @Transactional
    override fun save(personDTO: PersonDTO): PersonDTO {
        var person: Person
        if (personDTO.uid?.isBlank() ?: true) {
            person = personRepo.save(
                Person(
                    uid = UUID.randomUUID().toString(),
                    name = personDTO.name,
                    address = Address(
                        city = personDTO.city,
                        street = personDTO.street,
                        houseNum = personDTO.houseNum
                    )
                )
            )
        } else {
            person = personRepo.findByUid(personDTO.uid!!)

            person.apply {
                uid = personDTO.uid!!
                name = personDTO.name
                address = Address(
                    city = personDTO.city,
                    street = personDTO.street,
                    houseNum = personDTO.houseNum
                )
            }
            person = personRepo.save(person)
        }
        return StructureMapper.structurePersonDto(person)
    }

    @Transactional
    override fun findAll(): List<PersonDTO?> = StructureMapper.structurePersonListDto(personRepo.findAll())

    @Transactional
    override fun deleteByUid(uid: String) = personRepo.deleteByUid(uid)

    @Transactional
    override fun updateOne(uid: String, personDTO: PersonDTO): PersonDTO {
        var person: Person =
            if (personDTO.uid?.isBlank() ?: true)
               personRepo.findByUid(uid)
             else
                personRepo.findByUid(personDTO.uid!!)

        person.apply {
            this.uid = uid
            name = personDTO.name
            address = Address(
                city = personDTO.city,
                street = personDTO.street,
                houseNum = personDTO.houseNum
            )
        }
        person = personRepo.save(person)

        return StructureMapper.structurePersonDto(person)
    }

    @Transactional
    override fun findByUid(uid: String): PersonDTO = StructureMapper.structurePersonDto(personRepo.findByUid(uid))
}
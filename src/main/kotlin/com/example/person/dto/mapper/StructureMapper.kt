package com.example.person.dto.mapper

import com.example.person.dto.PersonDTO
import com.example.person.model.Person

object StructureMapper {
    fun structurePersonDto(person: Person) = PersonDTO(
        uid = person.uid,
        name = person.name,
        city = person.address.city,
        street = person.address.street,
        houseNum = person.address.houseNum
    )

    fun structurePersonListDto(personList: List<Person>) = personList.map {
        PersonDTO(
            uid = it.uid,
            name = it.name,
            city = it.address.city,
            street = it.address.street,
            houseNum = it.address.houseNum
        )
    }
}

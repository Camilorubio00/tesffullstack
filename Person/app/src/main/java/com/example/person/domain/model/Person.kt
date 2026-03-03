package com.example.person.domain.model

import com.example.person.data.model.PersonDto

data class Person(
    val id: Int? = null,
    val name: String? = null,
    val lastName: String? = null,
    val birthdate: String? = null,
    val job: String? = null,
    val salary: Double? = null
)

fun Person.toPersonDto() = PersonDto(
    id = id,
    nombre = name,
    apellido = lastName,
    fechaNacimiento = birthdate,
    puesto = job,
    sueldo = salary
)
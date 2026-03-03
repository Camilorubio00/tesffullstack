package com.example.person.data.model

import com.example.person.domain.model.Person

data class ResponseDto(
    val status: Boolean? = null,
    val msg: String? = null,
    val data: List<PersonDto>,
)

data class PersonDto(
    val id: Int? = null,
    val nombre: String? = null,
    val apellido: String? = null,
    val fechaNacimiento: String? = null,
    val puesto: String? = null,
    val sueldo: Double? = null
)

fun List<PersonDto>.toPersonList() = map { it.toPerson() }

fun PersonDto.toPerson() = Person(
    id = id ?: 0,
    name = nombre ?: "",
    lastName = apellido ?: "",
    birthdate = fechaNacimiento ?: "",
    job = puesto ?: "",
    salary = sueldo ?: 0.0
)
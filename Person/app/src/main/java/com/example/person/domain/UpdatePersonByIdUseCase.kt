package com.example.person.domain

import com.example.person.data.repository.PersonRepository
import com.example.person.domain.model.Person
import com.example.person.extensions.Result
import javax.inject.Inject

class UpdatePersonByIdUseCase @Inject constructor(private val personRepository: PersonRepository) {

    suspend operator fun invoke(
        id: Int?,
        name: String?,
        lastName: String?,
        birthdate: String?,
        job: String?,
        salary: Double?): Result<List<Person>> {
        val person = Person(
            id = id,
            name = name,
            lastName = lastName,
            birthdate = birthdate,
            job = job,
            salary = salary
        )
        return personRepository.updatePerson(person)
    }
}
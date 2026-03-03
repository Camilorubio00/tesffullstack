package com.example.person.domain

import com.example.person.data.repository.PersonRepository
import com.example.person.domain.model.Person
import com.example.person.extensions.Result
import javax.inject.Inject

class CreatePersonUseCase @Inject constructor(private val personRepository: PersonRepository) {

    suspend operator fun invoke(
        name: String?,
        lastName: String?,
        birthdate: String?,
        job: String?,
        salary: Double?
    ): Result<List<Person>> {
        val person = Person(
            name = name,
            lastName = lastName,
            birthdate = birthdate,
            job = job,
            salary = salary
        )
        val result = personRepository.createPerson(person)
        if (result is Result.Success) {
            return Result.Success(result.data)
        }
        return result
    }
}
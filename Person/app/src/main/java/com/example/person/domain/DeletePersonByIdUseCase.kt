package com.example.person.domain

import com.example.person.data.repository.PersonRepository
import com.example.person.domain.model.Person
import com.example.person.extensions.Result
import javax.inject.Inject

class DeletePersonByIdUseCase @Inject constructor(private val personRepository: PersonRepository) {

    suspend operator fun invoke(currentlyPersonList: List<Person>, id: Int): Result<List<Person>> {
        val result = personRepository.deletePersonBy(id)
        if (result is Result.Success) {
            val updatedList = currentlyPersonList.filterNot { it.id == id }
            return Result.Success(updatedList)
        }
        return personRepository.deletePersonBy(id)
    }
}
package com.example.person.domain

import com.example.person.data.repository.PersonRepository
import javax.inject.Inject

class FetchPersonByIdUseCase @Inject constructor(private val personRepository: PersonRepository) {

    suspend operator fun invoke(id: Int) = personRepository.fetchPersonBy(id)
}
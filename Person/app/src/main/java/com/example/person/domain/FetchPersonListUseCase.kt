package com.example.person.domain

import com.example.person.data.repository.PersonRepository
import javax.inject.Inject

class FetchPersonListUseCase @Inject constructor(private val personRepository: PersonRepository) {

    suspend operator fun invoke() = personRepository.fetchPersonList()
}
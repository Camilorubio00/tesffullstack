package com.example.person.data.repository

import com.example.person.data.datasource.PersonRemoteDataSource
import com.example.person.domain.model.Person
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val personRemoteDataSource: PersonRemoteDataSource
) {
    suspend fun fetchPersonList() = personRemoteDataSource.fetchPersonList()

    suspend fun createPerson(person: Person) = personRemoteDataSource.createPerson(person)

    suspend fun updatePerson(person: Person) =
        personRemoteDataSource.updatePersonBy(person)

    suspend fun fetchPersonBy(id: Int) = personRemoteDataSource.fetchPersonBy(id)

    suspend fun deletePersonBy(id: Int) = personRemoteDataSource.deletePersonBy(id)
}
package com.example.person.data.datasource

import com.example.person.data.PersonApi
import com.example.person.data.model.toPersonList
import com.example.person.domain.model.Person
import com.example.person.domain.model.toPersonDto
import com.example.person.exceptions.ApiExceptionHandler
import com.example.person.extensions.Result.Success
import com.example.person.extensions.Result.Error
import javax.inject.Inject

class PersonRemoteDataSource @Inject constructor(
    private val personApi: PersonApi,
    private val apiExceptionHandler: ApiExceptionHandler
) {
    suspend fun fetchPersonList() = try {
        val result = personApi.fetchPersonList()
        Success(result.data.toPersonList())
    } catch (exception: Exception) {
        Error(apiExceptionHandler.cause(exception))
    }

    suspend fun createPerson(person: Person) = try {
        val result = personApi.createPerson(person.toPersonDto())
        Success(result.data.toPersonList())
    } catch (exception: Exception) {
        Error(apiExceptionHandler.cause(exception))
    }

    suspend fun updatePersonBy(person: Person) = try {
        val result = personApi.updatePersonBy(person.id.toString(), person.toPersonDto())
        Success(result.data.toPersonList())
    } catch (exception: Exception) {
        Error(apiExceptionHandler.cause(exception))
    }

    suspend fun fetchPersonBy(id: Int) = try {
        val result = personApi.fetchPersonBy(id)
        Success(result.data.toPersonList())
    } catch (exception: Exception) {
        Error(apiExceptionHandler.cause(exception))
    }

    suspend fun deletePersonBy(id: Int) = try {
        val result = personApi.deletePersonBy(id)
        Success(result.data.toPersonList())
    } catch (exception: Exception) {
        Error(apiExceptionHandler.cause(exception))
    }
}
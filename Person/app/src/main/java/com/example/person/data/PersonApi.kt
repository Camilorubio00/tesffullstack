package com.example.person.data

import com.example.person.data.model.PersonDto
import com.example.person.data.model.ResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Body
import retrofit2.http.Path

interface PersonApi {

    @GET(PERSON)
    suspend fun fetchPersonList(): ResponseDto

    @POST(PERSON)
    suspend fun createPerson(@Body personDto: PersonDto): ResponseDto

    @PUT("$PERSON/{id}")
    suspend fun updatePersonBy(@Path("id") id: String, @Body personDto: PersonDto): ResponseDto

    @GET("$PERSON/{id}")
    suspend fun fetchPersonBy(@Path("id") id: Int): ResponseDto

    @DELETE("$PERSON/{id}")
    suspend fun deletePersonBy(@Path("id") id: Int): ResponseDto
}
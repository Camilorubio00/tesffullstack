package com.example.person.di

import com.example.person.core.CoroutinesDispatchers
import com.example.person.data.BASE_URL
import com.example.person.data.PersonApi
import com.example.person.exceptions.ApiExceptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainActivityModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .readTimeout(20, SECONDS)
            .connectTimeout(20, SECONDS)
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient) : PersonApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PersonApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApiExceptionHandler() : ApiExceptionHandler {
        return ApiExceptionHandler()
    }

    @Provides
    @Singleton
    fun provideCoroutinesDispatchers() = CoroutinesDispatchers()
}
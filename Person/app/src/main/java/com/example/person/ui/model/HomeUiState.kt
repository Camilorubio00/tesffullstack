package com.example.person.ui.model

import com.example.person.domain.model.Person

data class HomeUiState(
    val isLoading: Boolean = false,
    val persons: List<Person> = emptyList(),
    val errorMessage: String? = null
)
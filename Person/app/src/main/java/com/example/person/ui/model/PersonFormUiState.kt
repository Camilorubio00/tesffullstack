package com.example.person.ui.model

data class PersonFormUiState(
    val isLoading: Boolean = false,
    val personId: Int? = null,
    val name: String = "",
    val lastName: String = "",
    val birthdate: String = "",
    val job: String = "",
    val salaryText: String = "",
    val errorMessage: String? = null,
    val isEditMode: Boolean = false
)
package com.example.person

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.person.core.CoroutinesDispatchers
import com.example.person.domain.CreatePersonUseCase
import com.example.person.domain.FetchPersonByIdUseCase
import com.example.person.domain.UpdatePersonByIdUseCase
import com.example.person.extensions.Result
import com.example.person.ui.model.PersonFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PersonFormViewModel @Inject constructor(
    private val fetchPersonByIdUseCase: FetchPersonByIdUseCase,
    private val createPersonUseCase: CreatePersonUseCase,
    private val updatePersonByIdUseCase: UpdatePersonByIdUseCase,
    private val dispatchers: CoroutinesDispatchers
) : ViewModel() {

    var uiState by mutableStateOf(PersonFormUiState())
        private set

    fun resetForCreate() {
        uiState = PersonFormUiState()
    }

    fun loadPerson(id: Int) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch(dispatchers.io) {
            when (val result = fetchPersonByIdUseCase(id)) {
                is Result.Success -> {
                    val person = result.data.firstOrNull()
                    uiState =
                        if (person != null) {
                            uiState.copy(
                                isLoading = false,
                                personId = person.id,
                                name = person.name ?: "",
                                lastName = person.lastName ?: "",
                                birthdate = person.birthdate ?: "",
                                job = person.job ?: "",
                                salaryText = person.salary.toString(),
                                isEditMode = true,
                                errorMessage = null
                            )
                        } else {
                            uiState.copy(
                                isLoading = false,
                                errorMessage = "No se encontró la persona",
                                isEditMode = true
                            )
                        }
                }

                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )
                }
            }
        }
    }

    fun updateName(value: String) {
        uiState = uiState.copy(name = value)
    }

    fun updateLastName(value: String) {
        uiState = uiState.copy(lastName = value)
    }

    fun updateJob(value: String) {
        uiState = uiState.copy(job = value)
    }

    fun onBirthdateChangeFromPicker(year: Int, month: Int, dayOfMonth: Int) {
        val m = month + 1
        val dateString = String.format(Locale.US, "%04d-%02d-%02d", year, m, dayOfMonth)
        uiState = uiState.copy(birthdate = dateString)
    }

    fun updateSalaryText(value: String) {
        uiState = uiState.copy(salaryText = value)
    }

    fun submit(onFinished: (Boolean) -> Unit) {
        val name = uiState.name.trim()
        val lastName = uiState.lastName.trim()
        val birthdate = uiState.birthdate.trim()
        val job = uiState.job.trim()
        val salary = uiState.salaryText.toDoubleOrNull()

        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch(dispatchers.io) {
            val result =
                if (uiState.isEditMode && uiState.personId != null) {
                    updatePersonByIdUseCase(
                        id = uiState.personId,
                        name = name,
                        lastName = lastName,
                        birthdate = birthdate,
                        job = job,
                        salary = salary
                    )
                } else {
                    createPersonUseCase(
                        name = name,
                        lastName = lastName,
                        birthdate = birthdate,
                        job = job,
                        salary = salary
                    )
                }

            when (result) {
                is Result.Success -> {
                    uiState = uiState.copy(isLoading = false, errorMessage = null)
                    onFinished(true)
                }

                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Error al guardar"
                    )
                    onFinished(false)
                }
            }
        }
    }
}


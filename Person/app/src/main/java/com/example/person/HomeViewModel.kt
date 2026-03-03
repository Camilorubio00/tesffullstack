package com.example.person

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.person.core.CoroutinesDispatchers
import com.example.person.domain.DeletePersonByIdUseCase
import com.example.person.domain.FetchPersonListUseCase
import com.example.person.extensions.Result
import com.example.person.ui.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPersonListUseCase: FetchPersonListUseCase,
    private val deletePersonByIdUseCase: DeletePersonByIdUseCase,
    private val dispatchers: CoroutinesDispatchers
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    fun loadPersons() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch(dispatchers.io) {
            when (val result = fetchPersonListUseCase()) {
                is Result.Success -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        persons = result.data,
                        errorMessage = null
                    )
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

    fun deletePerson(id: Int) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch(dispatchers.io) {
            val currentList = uiState.persons
            when (val result = deletePersonByIdUseCase(currentList, id)) {
                is Result.Success -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        persons = result.data,
                        errorMessage = null
                    )
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
}


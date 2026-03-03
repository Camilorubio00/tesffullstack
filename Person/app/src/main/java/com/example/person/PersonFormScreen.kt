package com.example.person

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonFormScreen(
    personId: Int?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: PersonFormViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(personId) {
        if (personId != null) {
            viewModel.loadPerson(personId)
        } else {
            viewModel.resetForCreate()
        }
    }

    val calendar = remember { Calendar.getInstance() }

    val openDatePicker: () -> Unit = {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, y, m, d ->
                viewModel.onBirthdateChangeFromPicker(y, m, d)
            },
            year,
            month,
            day
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (state.isEditMode) "Editar persona" else "Crear persona")
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else {
                state.errorMessage?.let { message ->
                    LaunchedEffect(message) {
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { value ->
                        if (value.length <= 40) {
                            viewModel.updateName(value)
                        }
                    },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = { value ->
                        if (value.length <= 40) {
                            viewModel.updateLastName(value)
                        }
                    },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.job,
                    onValueChange = { value ->
                        if (value.length <= 60) {
                            viewModel.updateJob(value)
                        }
                    },
                    label = { Text("Puesto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.birthdate,
                        onValueChange = {},
                        label = { Text("Fecha de nacimiento (AAAA-MM-DD)") },
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = openDatePicker) {
                        Text("Elegir fecha")
                    }
                }

                OutlinedTextField(
                    value = state.salaryText,
                    onValueChange = { value ->
                        val filtered = value.filter { it.isDigit() || it == '.' }
                        if (filtered.count { it == '.' } <= 1 && filtered.length <= 10) {
                            viewModel.updateSalaryText(filtered)
                        }
                    },
                    label = { Text("Sueldo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onCancel) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            viewModel.submit { success ->
                                if (success) {
                                    onSaved()
                                }
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}


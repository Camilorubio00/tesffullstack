package com.example.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.person.domain.model.Person
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCreateClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadPersons()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Personas") })
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
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
                HomeContent(
                    persons = state.persons,
                    onCreateClick = onCreateClick,
                    onEditClick = onEditClick,
                    onDeleteClick = { id -> viewModel.deletePerson(id) }
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    persons: List<Person>,
    onCreateClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onCreateClick) {
                Text("Crear")
            }
        }
        if (persons.isEmpty()) {
            Text("No hay personas registradas.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(persons) { person ->
                    PersonItem(
                        person = person,
                        onEditClick = { onEditClick(person.id ?: 0) },
                        onDeleteClick = { onDeleteClick(person.id ?: 0) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PersonItem(
    person: Person,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${person.name} ${person.lastName}")
            Text("Nacimiento: ${person.birthdate}")
            Text("Puesto: ${person.job}")
            Text("Sueldo: ${person.salary}")
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onEditClick) {
                    Text("Editar")
                }
                Button(onClick = onDeleteClick) {
                    Text("Borrar")
                }
            }
        }
    }
}


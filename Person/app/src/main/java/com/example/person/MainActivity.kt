package com.example.person

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.person.ui.theme.PersonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonTheme {
                PersonApp()
            }
        }
    }
}

private sealed class Screen {
    data object Home : Screen()
    data class Form(val personId: Int?) : Screen()
}

@Composable
fun PersonApp() {
    val currentScreen = remember { mutableStateOf<Screen>(Screen.Home) }

    when (val screen = currentScreen.value) {
        is Screen.Home -> HomeScreen(
            onCreateClick = { currentScreen.value = Screen.Form(null) },
            onEditClick = { id -> currentScreen.value = Screen.Form(id) }
        )

        is Screen.Form -> PersonFormScreen(
            personId = screen.personId,
            onSaved = { currentScreen.value = Screen.Home },
            onCancel = { currentScreen.value = Screen.Home }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonAppPreview() {
    PersonTheme {
        PersonApp()
    }
}
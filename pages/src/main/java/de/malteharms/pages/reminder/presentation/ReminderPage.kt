package de.malteharms.pages.reminder.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import de.malteharms.pages.components.presentation.TopBar
import de.malteharms.pages.reminder.data.ReminderState

@Composable
fun ReminderPage(
    navController: NavController,
    state: ReminderState
) {



    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = "Reminder",
                navigateBack = false
            )
        }
    ) { paddingValues ->

        LazyColumn(contentPadding = paddingValues) {

            items() {

            }

        }

    }


}
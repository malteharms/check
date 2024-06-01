package de.malteharms.check.pages.food.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import de.malteharms.check.presentation.components.TopBar

@Composable
fun Food(
    navController: NavController
) {

    // TODO load current week from viewModel

    Scaffold(
        topBar = { TopBar(navController, "Food") }
    ) { paddingValues ->



    }
}

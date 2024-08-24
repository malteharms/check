package de.malteharms.pages.food.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Food(
    navController: NavController
) {

    // TODO load current week from viewModel

    Scaffold(
        topBar = { de.malteharms.pages.components.presentation.TopBar(navController, "Food") },
        bottomBar = {
            de.malteharms.pages.components.presentation.FloatingBottomNavigation(
                navController,
                de.malteharms.pages.components.data.getBottomNavigationItems(),
                "Food"
            )
                    },
    ) { _->



    }
}

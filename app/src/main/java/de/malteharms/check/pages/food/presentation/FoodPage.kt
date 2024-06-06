package de.malteharms.check.pages.food.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import de.malteharms.check.data.getBottomNavigationItems
import de.malteharms.check.presentation.components.FloatingBottomNavigation
import de.malteharms.check.presentation.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Food(
    navController: NavController
) {

    // TODO load current week from viewModel

    Scaffold(
        topBar = { TopBar(navController, "Food") },
        bottomBar = {
            FloatingBottomNavigation( navController, getBottomNavigationItems(), "Food")
                    },
    ) { _->



    }
}

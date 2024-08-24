package de.malteharms.pages.home.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import de.malteharms.pages.components.data.getBottomNavigationItems
import de.malteharms.pages.components.presentation.FloatingBottomNavigation
import de.malteharms.pages.components.presentation.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    navController: NavController
) {
    Scaffold(
        topBar = { TopBar(navController, "Home") },
        bottomBar = {
            FloatingBottomNavigation( navController, getBottomNavigationItems(), "Home")
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Homescreen",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

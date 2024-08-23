package de.malteharms.pages.ui

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Cash(
    navController: NavController
) {
    Scaffold(
        topBar = { de.malteharms.pages.components.presentation.TopBar(navController, "Cash") },
        bottomBar = {
            de.malteharms.pages.components.presentation.FloatingBottomNavigation(
                navController,
                de.malteharms.pages.components.data.getBottomNavigationItems(),
                "Costs"
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Cash is currently in development",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                minLines = 2
            )
        }
    }
}

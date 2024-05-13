package de.malteharms.check.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.data.Screens
import de.malteharms.check.ui.theme.blue80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    title: String
) {

    TopAppBar(
        modifier = Modifier.height(80.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = blue80,
            titleContentColor = Color.White,
        ),
        title = {
            Text(title)
        },
        actions = {

            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = "Localized description"
                )
            }

            IconButton(onClick = {
                navController.navigate(Screens.SettingsRoute.route)
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            }
        },
    )
}
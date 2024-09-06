package de.malteharms.pages.components.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import de.malteharms.pages.components.data.Screens

@Composable
fun TopBar(
    navController: NavController,
    title: String,
    navigateBack: Boolean = false
) {

    val backgroundColor: Color = MaterialTheme.colorScheme.background
    val onBackGroundColor: Color = MaterialTheme.colorScheme.onBackground

    Box (
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            // Icon for navigate back and title of the page
            Row (verticalAlignment = Alignment.CenterVertically){
                if (navigateBack) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = onBackGroundColor
                        )
                    }
                }
                Text(
                    text = title,
                    color = onBackGroundColor,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }

            // Profile icon to navigate to the profile and settings screen
            IconButton(onClick = { navController.navigate(Screens.SettingsRoute.route) }) {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = null,
                    tint = onBackGroundColor
                )
            }

        }

        // HorizontalDivider()
    }
}

@Preview
@Composable
fun AppBarPreview() {
    TopBar(
        rememberNavController(),
        "Home",
        true
    )
}


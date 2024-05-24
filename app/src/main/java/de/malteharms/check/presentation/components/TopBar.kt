package de.malteharms.check.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.data.Screens

@Composable
fun TopBar(
    navController: NavController,
    title: String
) {

    val backgroundColor: Color = MaterialTheme.colorScheme.background
    val onBackGroundColor: Color = MaterialTheme.colorScheme.onBackground

    Box (
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = onBackGroundColor,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight
                )

                Row(horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = {
                        navController.navigate(Screens.ProfileRoute.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Face,
                            contentDescription = null,
                            tint = onBackGroundColor
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(Screens.SettingsRoute.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = onBackGroundColor
                        )
                    }
                }
            }

            HorizontalDivider()
        }
    }

}
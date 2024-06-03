package de.malteharms.check.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    var search by remember {
        mutableStateOf("")
    }

    Box (
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                color = onBackGroundColor,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )

            CustomTextField(
                value = search,
                onValueChange = { newValue -> search = newValue},
                placeholderText = "Search for Todos, Reminder, ...",
                trailingIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.SettingsRoute.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Face,
                            contentDescription = null,
                            tint = onBackGroundColor
                        )
                    }
                },
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(vertical = 3.dp, horizontal = 5.dp)
            )

            // HorizontalDivider()
        }
    }

}
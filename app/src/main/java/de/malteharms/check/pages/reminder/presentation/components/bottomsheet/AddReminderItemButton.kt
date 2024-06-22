package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddReminderItemButton(
    onClick: () -> Unit
) {

    ExtendedFloatingActionButton(
        modifier = Modifier.padding(bottom = 10.dp),

        onClick = { onClick() },

        text = { Text(
            text = "Add Item",
            color = MaterialTheme.colorScheme.onPrimary
        )},
        icon = { Icon(Icons.Filled.Add, null) },

        containerColor = MaterialTheme.colorScheme.primary
    )

}
package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.malteharms.check.pages.reminder.data.ReminderData


@Composable
fun ReminderBottomSheet(item: ReminderData?) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (item == null) {
                // Add new reminder
                AddReminderItemBottomSheet()

            } else {
                // Edit existing reminder
                EditReminderItemBottomSheet(item)
            }
        }
    }
}


@Composable
fun AddReminderItemBottomSheet() {
    val (titleState, setTitleState) = remember { mutableStateOf("") }
    val (descriptionState, setDescriptionState) = remember { mutableStateOf("") }

    OutlinedTextField(
        value = titleState,
        onValueChange = setTitleState,
        label = { Text("Titel") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = descriptionState,
        onValueChange = setDescriptionState,
        label = { Text("Beschreibung") },
        modifier = Modifier.fillMaxWidth()
    )

    // todo add date picker here

    // Add logic to pick an icon (e.g., IconButton with selection functionality)
    Button(onClick = { /* Add new reminder logic */ }) {
        Text("Erinnerung hinzufügen")
    }
}


@Composable
fun EditReminderItemBottomSheet(item: ReminderData) {
    val (titleState, setTitleState) = remember { mutableStateOf("") }
    val (descriptionState, setDescriptionState) = remember { mutableStateOf("") }

    // todo show existing values inside the text box

    Text(text = "Titel:")
    OutlinedTextField(
        value = titleState,
        onValueChange = setTitleState,
        label = { Text("") }, // No label for existing title
        modifier = Modifier.fillMaxWidth()
    )
    Text(text = "Kategorie:")
    Text(text = item.iconId.toString()) // Assuming iconId is an Int
    // Disable icon selection for existing reminders (optional)
    Text(text = "Beschreibung:")
    OutlinedTextField(
        value = descriptionState,
        onValueChange = setDescriptionState,
        label = { Text("") }, // No label for existing description
        modifier = Modifier.fillMaxWidth()
    )
    Text(text = "Fällig am:")

    // todo add date picker here

    Button(onClick = { /* Update reminder logic */ }) {
        Text("Erinnerung aktualisieren")
    }
}
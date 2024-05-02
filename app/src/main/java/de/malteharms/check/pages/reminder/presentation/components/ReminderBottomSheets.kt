package de.malteharms.check.pages.reminder.presentation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.malteharms.check.pages.reminder.data.ReminderData
import de.malteharms.check.pages.reminder.domain.validateNewReminderEntry
import de.malteharms.check.ui.components.LeadingIconWithText
import de.malteharms.check.ui.theme.blue80
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
    var title by remember { mutableStateOf(TextFieldValue("")) }

    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd. MMM yyyy")
                .format(pickedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()

    Row (
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        TextField(
            value = title,
            onValueChange = { newText -> title = newText },
            placeholder = { Text(text = "Titel") },
            modifier = Modifier.background(Color.Transparent),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }

    LeadingIconWithText(
        icon = Icons.Default.DateRange,
        text = formattedDate,
        modifier = Modifier.clickable { dateDialogState.show() }
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Add logic to pick an icon (e.g., IconButton with selection functionality)

    Button(
        onClick = { /* todo validate and send item to server */ },
        colors = ButtonDefaults.buttonColors(containerColor = blue80)
    ) {
        Text("Erinnerung hinzufügen")
    }

    // date picker
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ) {
        val currentDate = LocalDate.now()

        datepicker(
            initialDate = currentDate,
            title = "Wähle ein Datum",
            allowedDateValidator = { it.isAfter(currentDate) }
        ) {
            pickedDate = it
        }
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
package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.data.convertTimestampToDateString
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.CategoryChoice
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.EditableNotificationRow
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.EditableTitleRow
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun ReminderBottomSheet(
    item: ReminderItem?,
    state: ReminderState,
    onEvent: (ReminderEvent) -> Unit
) {
    val dateDialogState = rememberMaterialDialogState()

    var title: TextFieldValue by remember {
        mutableStateOf(TextFieldValue(text = item?.title ?: ""))
    }

    var pickedDate: LocalDateTime by remember {
        mutableStateOf(item?.dueDate ?: LocalDateTime.now())
    }

    val formattedDate: String by remember {
        derivedStateOf {
            convertTimestampToDateString(pickedDate)
        }
    }

    val editableRowModifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
    val editableRowAlignment = Alignment.CenterVertically
    val editableRowArrangement = Arrangement.spacedBy(20.dp)

    val editable: Boolean = item?.birthdayRelation == null

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
    ) {
        
        // title row
        EditableTitleRow(
            title = title.text,
            editable = editable,
            onValueChange = { newText ->
                title = TextFieldValue(newText)
                onEvent(ReminderEvent.SetTitle(newText))
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        // category row
        CategoryChoice(
            item = item,
            onEvent = onEvent,
            editable = editable
        )


        // date picker row
        Row(
            modifier = editableRowModifier.clickable {
                if (editable) {
                    dateDialogState.show()
                }
            },
            verticalAlignment = editableRowAlignment,
            horizontalArrangement = editableRowArrangement
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            Text(text = formattedDate, color = if (editable) { MaterialTheme.colorScheme.onBackground } else Color.Gray)
        }

        EditableNotificationRow(
            modifier = editableRowModifier,
            arrangement = editableRowArrangement,
            state = state,
            onEvent = onEvent
        )

        // bottom row to display event buttons
        Spacer(modifier = Modifier.height(20.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            if (item == null) {
                Button(
                    onClick = { onEvent(ReminderEvent.SaveItem) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text(
                    text = "Reminder speichern",
                    color = MaterialTheme.colorScheme.onSecondary
                ) }
            } else {
                Button(
                    onClick = { onEvent(ReminderEvent.RemoveItem(item)) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text(
                    text = "Reminder löschen",
                    color = MaterialTheme.colorScheme.onError
                ) }
            }
        }

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
            ) { newDate: LocalDate ->
                pickedDate = newDate.atStartOfDay()
                onEvent(ReminderEvent.SetDueDate(pickedDate))
            }
        }
    }

}


@Preview
@Composable
fun ReminderBottomSheetPreview() {

    val sampleItem = ReminderItem(
        id = -1,
        title = "Perso",
        category = ReminderCategory.GENERAL,
        todoRelation = null,
        dueDate = LocalDateTime.now(),
        creationDate = LocalDateTime.now(),
        lastUpdate = LocalDateTime.now()
    )

    ReminderBottomSheet(
        item = sampleItem,
        state = ReminderState(),
        onEvent = {  }
    )
}

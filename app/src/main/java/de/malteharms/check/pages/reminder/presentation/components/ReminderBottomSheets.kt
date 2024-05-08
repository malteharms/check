package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import de.malteharms.check.pages.reminder.data.ReminderItem
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.domain.convertLocalDateToTimestamp
import de.malteharms.check.pages.reminder.domain.convertTimestampToDateString
import de.malteharms.check.pages.reminder.domain.getCurrentTimestamp
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.CategoryChoice
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.EditableNotificationRow
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.EditableTitleRow
import de.malteharms.check.pages.reminder.presentation.getAddOrUpdateButtonText
import de.malteharms.check.ui.components.LeadingIconWithText
import de.malteharms.check.ui.theme.blue80
import java.time.LocalDate


@Composable
fun ReminderBottomSheet(
    item: ReminderItem?,
    onEvent: (ReminderEvent) -> Unit
) {
    val dateDialogState = rememberMaterialDialogState()

    var title by remember {
        mutableStateOf(TextFieldValue(text = item?.title ?: ""))
    }

    var pickedDate by remember {
        mutableLongStateOf(item?.dueDate ?: getCurrentTimestamp())
    }

    val formattedDate by remember {
        derivedStateOf {
            convertTimestampToDateString(pickedDate)
        }
    }

    var notification by remember {
        mutableStateOf(item?.notification)
    }

    val editableRowModifier = Modifier.fillMaxWidth()
    val editableRowAlignment = Alignment.CenterVertically
    val editableRowArrangement = Arrangement.spacedBy(10.dp)

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
    ) {

        // category row
        CategoryChoice(
            item = item,
            onEvent = onEvent
        )

        // title row
        EditableTitleRow(
            modifier = editableRowModifier,
            alignment = editableRowAlignment,
            arrangement = editableRowArrangement,
            title = title.text,
            onValueChange = { newText ->
                title = TextFieldValue(newText)
                onEvent(ReminderEvent.SetTitle(newText))
            }
        )

        // date picker row
        Row(
            modifier = editableRowModifier.clickable { dateDialogState.show() },
            verticalAlignment = editableRowAlignment,
            horizontalArrangement = editableRowArrangement
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            Text(text = formattedDate)
        }

        EditableNotificationRow(
            modifier = editableRowModifier,
            alignment = editableRowAlignment,
            arrangement = editableRowArrangement,
            date = notification,
            onValueChange = { newText ->
                title = TextFieldValue(newText)
                onEvent(ReminderEvent.SetTitle(newText))
            }
        )


        // bottom row to display event buttons
        Spacer(modifier = Modifier.height(20.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            if (item != null) {
                Button(
                    onClick = {
                        onEvent(ReminderEvent.RemoveItem(item))

                        // this hide dialog is necessary, because the
                        // bottom sheet would not close correctly
                        // the reason isn't known yet
                        onEvent(ReminderEvent.HideDialog)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Reminder Löschen")
                }
            }

            Button(
                onClick = {
                    if (item == null) {
                        onEvent(ReminderEvent.SaveItem)
                    } else {
                        onEvent(ReminderEvent.UpdateItem(item))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = blue80)
            ) {
                Text(text = getAddOrUpdateButtonText(isItemNew = item == null))
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
                allowedDateValidator = { it.isAfter(currentDate) }
            ) {
                pickedDate = convertLocalDateToTimestamp(it)
                onEvent(ReminderEvent.SetDueDate(pickedDate))
            }
        }
    }




}


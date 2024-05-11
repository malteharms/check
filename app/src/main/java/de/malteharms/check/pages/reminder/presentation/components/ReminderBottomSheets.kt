package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.malteharms.check.pages.reminder.data.database.ReminderCategory
import de.malteharms.check.pages.reminder.data.database.ReminderItem
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
import de.malteharms.check.pages.reminder.data.database.ReminderNotificationInterval
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.domain.convertLocalDateToTimestamp
import de.malteharms.check.pages.reminder.domain.convertTimestampToDateString
import de.malteharms.check.pages.reminder.domain.getCurrentTimestamp
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.CategoryChoice
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.EditableNotificationRow
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.EditableTitleRow
import de.malteharms.check.pages.reminder.presentation.getAddOrUpdateButtonText
import de.malteharms.check.ui.theme.blue80
import java.time.LocalDate


@Composable
fun ReminderBottomSheet(
    item: ReminderItem?,
    notifications: List<ReminderNotification>,
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

    val currentNotifications by remember {
        mutableStateOf(notifications)
    }

    val editableRowModifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
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
            arrangement = editableRowArrangement,
            notifications = currentNotifications,
            onEvent = onEvent
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


@Preview
@Composable
fun ReminderBottomSheetPreview() {

    val sampleItem = ReminderItem(
        id = -1,
        title = "Perso",
        category = ReminderCategory.AUTOMATIC_RENEW,
        todoRelation = null,
        dueDate = 1715363412L,
        creationDate = 1715363412L,
        lastUpdate = 1715363412L
    )

    val sampleNotification = listOf(
        ReminderNotification(
            reminderItem = -1,
            valueBeforeDue = 5,
            interval = ReminderNotificationInterval.DAYS,
            notificationDate = 1715795411L
        )
    )

    ReminderBottomSheet(
        item = sampleItem,
        notifications = sampleNotification,
        onEvent = {  }
    )
}
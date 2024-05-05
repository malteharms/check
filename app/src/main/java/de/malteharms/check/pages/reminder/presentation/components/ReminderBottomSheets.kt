package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import de.malteharms.check.pages.reminder.presentation.getAddOrUpdateButtonText
import de.malteharms.check.ui.components.LeadingIconWithText
import de.malteharms.check.ui.theme.blue80
import java.time.LocalDate


@Composable
fun ReminderBottomSheet(
    item: ReminderItem?,
    onEvent: (ReminderEvent) -> Unit
) {

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

    val dateDialogState = rememberMaterialDialogState()

    Row (
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        TextField(
            value = title,
            onValueChange = { newText -> title = newText },
            placeholder = { Text(text = "Titel", color = Color.LightGray) },
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

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        if (item != null) {
            Button(
                onClick = {
                    onEvent(ReminderEvent.RemoveItem(item))
                    onEvent(ReminderEvent.HideDialog)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Reminder Löschen")
            }
        }

        Button(
            onClick = {
                onEvent(ReminderEvent.SetTitle(title.text))
                onEvent(ReminderEvent.SetDueDate(pickedDate))

                onEvent(ReminderEvent.SaveItem)
                onEvent(ReminderEvent.HideDialog)
            },
            colors = ButtonDefaults.buttonColors(containerColor = blue80)
        ) {
            Text(text = getAddOrUpdateButtonText(isItemNew = item == null))
        }
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
        ) { pickedDate = convertLocalDateToTimestamp(it) }
    }
}

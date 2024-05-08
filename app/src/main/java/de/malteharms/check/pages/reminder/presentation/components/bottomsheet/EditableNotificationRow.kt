package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.malteharms.check.pages.reminder.data.ReminderNotification

@Composable
fun EditableNotificationRow(
    modifier: Modifier,
    alignment: Alignment.Vertical,
    arrangement: Arrangement.Horizontal,

    notification: ReminderNotification?,
    onValueChange: (String) -> Unit,
) {



    Row (
        modifier = modifier,
        verticalAlignment = alignment,
        horizontalArrangement = arrangement
    ){

        Icon(imageVector = Icons.Default.Notifications, contentDescription = null)

        // TODO start here to implement a dialog for choosing a notification date
        Text(text = "" ?: "keine Benachrichtigung")


    }

}
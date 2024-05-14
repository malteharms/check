package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.pages.reminder.presentation.getNotificationText

@Composable
fun LoadedNotificationRow(
    reminderNotification: ReminderNotification,
    onDelete: (ReminderNotification) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = getNotificationText(reminderNotification))
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.clickable { onDelete(reminderNotification) }
        )
    }
}
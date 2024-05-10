package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
import de.malteharms.check.pages.reminder.domain.ReminderEvent

@Composable
fun DisplayedNotificationRow(
    modifier: Modifier,
    arrangement: Arrangement.Horizontal,
    isFirstRow: Boolean,
    currentNotification: ReminderNotification?,
    openAddReminderDialog: () -> Unit,
    removeNotification: () -> Unit
) {

    val iconColor = if (isFirstRow) LocalContentColor.current else Color.Transparent

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = arrangement
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = iconColor
        )

        if (currentNotification == null) {
            TextButton(onClick = { openAddReminderDialog() }) {
                Text("+ Neue Benachrichtigung")
            }
        } else {
            LoadedNotificationRow(
                reminderNotification = currentNotification,
                onDelete = { removeNotification() }
            )
        }
    }
}
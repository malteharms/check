package de.malteharms.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.malteharms.database.tables.NotificationItem

@Composable
fun DisplayedNotificationRow(
    modifier: Modifier,
    arrangement: Arrangement.Horizontal,
    isFirstRow: Boolean,
    currentNotification: de.malteharms.database.tables.NotificationItem?,
    openAddReminderDialog: () -> Unit,
    removeNotification: () -> Unit
) {

    val iconColor: Color = if (isFirstRow) MaterialTheme.colorScheme.onBackground else Color.Transparent

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
            TextButton(
                onClick = { openAddReminderDialog() },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "+ Neue Benachrichtigung",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LoadedNotificationRow(
                reminderNotification = currentNotification,
                onDelete = { removeNotification() }
            )
        }
    }
}
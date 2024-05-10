package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
import de.malteharms.check.pages.reminder.data.database.ReminderNotificationInterval
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.presentation.getNotificationIntervalRepresentation
import de.malteharms.check.pages.reminder.presentation.getNotificationText
import de.malteharms.check.ui.components.LargeDropdownMenu

@Composable
fun EditableNotificationRow(
    modifier: Modifier,
    arrangement: Arrangement.Horizontal,

    notifications: List<ReminderNotification>,
    onEvent: (ReminderEvent) -> Unit
) {

    var showNotificationDialog by remember {
        mutableStateOf(false)
    }

    var currentNotifications by remember {
        mutableStateOf(notifications)
    }


    Row (
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = arrangement
    ){

        Icon(imageVector = Icons.Default.Notifications, contentDescription = null)

        Column(
        ) {
            currentNotifications.forEach {notification ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = getNotificationText(notification))
                    Icon(
                        imageVector = Icons.Default.Delete, 
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            currentNotifications -= notification
                            onEvent(ReminderEvent.RemoveNotification(notification)) 
                        }
                    )
                }
            }
            
            TextButton(onClick = { showNotificationDialog = true }) {
                Text("+ Neue Benachrichtigung")
            }
        }


    }

    if (showNotificationDialog) {

        var value by remember {
            mutableStateOf(TextFieldValue("1"))
        }

        var interval by remember {
            mutableStateOf(ReminderNotificationInterval.DAYS)
        }

        AlertDialog(
            icon = {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
            },
            title = {
                Text(text = "Benachrichtige mich")
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            value = it
                        }
                    )

                    LargeDropdownMenu(
                        label = "Interval",
                        items = ReminderNotificationInterval.entries,
                        selectedIndex = interval.ordinal,
                        onItemSelected = { _, item -> interval = item },
                        selectedItemToString = { getNotificationIntervalRepresentation(interval) },
                        modifier = Modifier.weight(3f)
                    )


                    Text(text = "vorher", modifier = Modifier.weight(1f))
                }
            },
            onDismissRequest = {
                showNotificationDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newReminderNotification = ReminderNotification(
                            reminderItem = -1,  // will be set on save
                            valueBeforeDue = value.text.toInt(),
                            interval = interval,
                            notificationDate = -1 // will be set on save
                        )

                        currentNotifications += newReminderNotification
                        onEvent(ReminderEvent.AddNotification(newReminderNotification))

                        showNotificationDialog = false
                    }
                ) {
                    Text("Hinzufügen")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNotificationDialog = false
                    }
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }

}
package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.data.database.tables.ReminderNotificationInterval
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.presentation.getNotificationIntervalRepresentation
import de.malteharms.check.presentation.components.LargeDropdownMenu
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult =  { isGranted ->
            showNotificationDialog = isGranted
        }
    )

    var currentNotifications by remember {
        mutableStateOf(notifications)
    }

    LazyColumn {

        itemsIndexed(currentNotifications) {index, item ->

            DisplayedNotificationRow(
                modifier = modifier,
                arrangement = arrangement,
                isFirstRow = index == 0,
                currentNotification = item,
                openAddReminderDialog = { showNotificationDialog = true },
                removeNotification = {
                    currentNotifications -= item
                    onEvent(ReminderEvent.RemoveNotification(item))
                }
            )

        }

        item {
            DisplayedNotificationRow(
                modifier = modifier,
                arrangement = arrangement,
                isFirstRow = currentNotifications.isEmpty(),
                currentNotification = null,
                openAddReminderDialog = {
                    notificationPermissionResultLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                },
                removeNotification = {
                    if (currentNotifications.isNotEmpty()) {
                        currentNotifications -= currentNotifications[0]
                        onEvent(ReminderEvent.RemoveNotification(currentNotifications[0]))
                    }
                }
            )
        }
    }

    if (showNotificationDialog) {

        var value by remember {
            mutableStateOf(TextFieldValue("1"))
        }

        var interval by remember {
            mutableStateOf(ReminderNotificationInterval.DAYS)
        }

        var warning by remember {
            mutableStateOf("")
        }

        AlertDialog(
            icon = {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
            },
            title = {
                Text(text = "Benachrichtige mich")
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        TextField(
                            modifier = Modifier.weight(1f),
                            value = value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = { newValue ->
                                value = newValue

                                warning = if (newValue.text.isEmpty()) {
                                    "Bitte gebe ein Zeitpunkt an"
                                } else if (!newValue.text.all { it.isDigit() }) {
                                    "Nur Ziffern sind erlaubt"
                                } else {
                                    if (newValue.text.toInt() < 0)
                                        "Zeitpunkt muss heute oder in der Zukunft liegen"
                                    else
                                        ""
                                }
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

                    // warning text
                    Text(text = warning, color = Color.Red)
                }
            },
            onDismissRequest = {
                showNotificationDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (warning == "") {
                            val newReminderNotification = ReminderNotification(
                                reminderItem = -1,  // will be set on save
                                valueBeforeDue = value.text.toInt(),
                                interval = interval,
                                notificationId = -1, // will be set on save
                                notificationDate = LocalDateTime.MAX // will be set on save
                            )

                            currentNotifications += newReminderNotification
                            onEvent(ReminderEvent.AddNotification(newReminderNotification))

                            showNotificationDialog = false
                        }
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
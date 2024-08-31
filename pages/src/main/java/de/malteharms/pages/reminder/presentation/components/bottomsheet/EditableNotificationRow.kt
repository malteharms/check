package de.malteharms.pages.reminder.presentation.components.bottomsheet

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import de.malteharms.pages.R
import de.malteharms.pages.reminder.data.ReminderState
import de.malteharms.pages.reminder.domain.ReminderEvent
import de.malteharms.pages.reminder.presentation.getNotificationIntervalRepresentation
import de.malteharms.pages.components.presentation.LargeDropdownMenu
import java.time.temporal.ChronoUnit


@Composable
fun EditableNotificationRow(
    modifier: Modifier,
    arrangement: Arrangement.Horizontal,

    state: ReminderState,
    onEvent: (ReminderEvent) -> Unit
) {

    val warnSpecifyDate: String = stringResource(R.string.specify_a_date)
    val warnOnlyDigits: String = stringResource(R.string.only_digits)
    val warnDateTodayOrFuture: String = stringResource(R.string.date_is_today_or_in_future)


    var showNotificationDialog by remember {
        mutableStateOf(false)
    }

    val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult =  { isGranted ->
            showNotificationDialog = isGranted
        }
    )

    LazyColumn {

        itemsIndexed(state.notifications) {index, item ->

            DisplayedNotificationRow(
                modifier = modifier,
                arrangement = arrangement,
                isFirstRow = index == 0,
                currentNotification = item,
                openAddReminderDialog = { showNotificationDialog = true },
                removeNotification = {
                    onEvent(ReminderEvent.RemoveNotification(item))
                }
            )
        }

        item {
            DisplayedNotificationRow(
                modifier = modifier,
                arrangement = arrangement,
                isFirstRow = state.notifications.isEmpty(),
                currentNotification = null,
                openAddReminderDialog = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionResultLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    } else {
                        showNotificationDialog = true
                    }
                },
                removeNotification = { }
            )
        }
    }

    if (showNotificationDialog) {

        var value by remember {
            mutableStateOf(TextFieldValue("1"))
        }

        var timeUnit by remember {
            mutableStateOf(ChronoUnit.DAYS)
        }

        var warning by remember {
            mutableStateOf("")
        }

        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            icon = { Icon(
                Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            ) },
            title = { Text(
                text = stringResource(R.string.notify_me),
                color = MaterialTheme.colorScheme.onBackground
            ) },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        TextField(
                            modifier = Modifier.weight(1f),
                            value = value,
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            maxLines = 1,
                            onValueChange = { newValue ->
                                value = newValue
                                warning = if (newValue.text.isEmpty()) {
                                    warnSpecifyDate
                                } else if (!newValue.text.all { it.isDigit() }) {
                                    warnOnlyDigits
                                } else {
                                    if (newValue.text.toInt() < 0)
                                        warnDateTodayOrFuture
                                    else
                                        ""
                                }
                            }
                        )

                        LargeDropdownMenu(
                            label = "Interval",
                            items = listOf(ChronoUnit.DAYS, ChronoUnit.MONTHS),
                            selectedIndex = timeUnit.ordinal,
                            onItemSelected = { _, item -> timeUnit = item },
                            selectedItemToString = { getNotificationIntervalRepresentation(timeUnit) },
                            modifier = Modifier.weight(3f)
                        )

                        Text(
                            text = "vorher",
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onBackground
                        )
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
                            onEvent(
                                ReminderEvent.AddDummyNotification(
                                value = value.text.toInt(),
                                timeUnit = timeUnit
                            ))
                            showNotificationDialog = false
                        }
                    }
                ) {
                    Text("Hinzufügen", color = MaterialTheme.colorScheme.onBackground)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNotificationDialog = false
                    }
                ) {
                    Text("Abbrechen", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
    }
}

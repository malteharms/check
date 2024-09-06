package de.malteharms.pages.reminder.data

import de.malteharms.database.tables.NotificationItem
import de.malteharms.database.tables.reminder.ReminderItem
import de.malteharms.utils.model.DateExt

data class ReminderState (

    val items: List<ReminderItem> = emptyList(),

    // store values for a new or existing ReminderItem, which is currently in editing mode
    val title: String = "",
    val due: DateExt? = null,
    val duration: DateExt? = null,
    val category: Long = 0,
    val isTodo: Boolean = false,
    val notifications: List<NotificationItem> = emptyList(),    // notifications for the open item

    // users state values
    val isAddingOrEditingItem: Boolean = false,

)
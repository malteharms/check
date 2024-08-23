package de.malteharms.check.pages.reminder.data

import de.malteharms.database.tables.ReminderCategory
import de.malteharms.database.tables.ReminderItem
import de.malteharms.database.tables.NotificationItem
import de.malteharms.utils.model.DateExt


data class ReminderState (

    val items: List<de.malteharms.database.tables.ReminderItem> = emptyList(),
    val allItems: List<de.malteharms.database.tables.ReminderItem> = emptyList(),
    val title: String = "",
    val dueDate: DateExt = DateExt.now(),
    val category: de.malteharms.database.tables.ReminderCategory = de.malteharms.database.tables.ReminderCategory.GENERAL,

    val notifications: List<de.malteharms.database.tables.NotificationItem> = listOf(),
    val newNotifications: List<de.malteharms.database.tables.NotificationItem> = listOf(),
    val notificationsToDelete: List<de.malteharms.database.tables.NotificationItem> = listOf(),

    val isAddingItem: Boolean = false,
    val isEditingItem: Boolean = false,
    val filter: List<de.malteharms.database.tables.ReminderCategory> = emptyList()
)

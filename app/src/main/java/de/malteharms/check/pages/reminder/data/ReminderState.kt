package de.malteharms.check.pages.reminder.data

import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.NotificationItem
import de.malteharms.utils.model.DateExt
import java.time.LocalDateTime


data class ReminderState (

    val items: List<ReminderItem> = emptyList(),
    val allItems: List<ReminderItem> = emptyList(),
    val title: String = "",
    val dueDate: DateExt = DateExt.now(),
    val category: ReminderCategory = ReminderCategory.GENERAL,

    val notifications: List<NotificationItem> = listOf(),
    val newNotifications: List<NotificationItem> = listOf(),
    val notificationsToDelete: List<NotificationItem> = listOf(),

    val isAddingItem: Boolean = false,
    val isEditingItem: Boolean = false,
    val filter: List<ReminderCategory> = emptyList()
)

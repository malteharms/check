package de.malteharms.check.pages.reminder.data

import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.ReminderNotification
import java.time.LocalDateTime


data class ReminderState (

    val items: List<ReminderItem> = emptyList(),
    val title: String = "",
    val dueDate: LocalDateTime = LocalDateTime.now(),
    val category: ReminderCategory = ReminderCategory.GENERAL,

    val notifications: List<ReminderNotification> = listOf(),
    val newNotifications: List<ReminderNotification> = listOf(),
    val notificationsToDelete: List<ReminderNotification> = listOf(),

    val isAddingItem: Boolean = false,
    val isEditingItem: Boolean = false,
    val filter: List<ReminderCategory> = emptyList()
)

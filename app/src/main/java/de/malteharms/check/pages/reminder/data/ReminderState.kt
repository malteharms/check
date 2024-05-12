package de.malteharms.check.pages.reminder.data

import de.malteharms.check.pages.reminder.data.database.ReminderCategory
import de.malteharms.check.pages.reminder.data.database.ReminderItem
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
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
    val sortType: ReminderSortType = ReminderSortType.DUE_DATE
)

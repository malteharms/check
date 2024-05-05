package de.malteharms.check.pages.reminder.data

import de.malteharms.check.pages.reminder.domain.getCurrentTimestamp

data class ReminderState (

    val items: List<ReminderItem> = emptyList(),
    val title: String = "",
    val dueDate: Long = 0L,
    val isAddingItem: Boolean = false,
    val isEditingItem: Boolean = false,
    val sortType: ReminderSortType = ReminderSortType.DUE_DATE
)

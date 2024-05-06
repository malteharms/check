package de.malteharms.check.pages.reminder.domain

import de.malteharms.check.pages.reminder.data.ReminderCategory
import de.malteharms.check.pages.reminder.data.ReminderItem
import de.malteharms.check.pages.reminder.data.ReminderSortType


sealed interface ReminderEvent {

    data object SaveItem: ReminderEvent
    data class UpdateItem(val itemToUpdate: ReminderItem): ReminderEvent
    data class RemoveItem(val item: ReminderItem): ReminderEvent

    data class SetTitle(val title: String): ReminderEvent
    data class SetDueDate(val dueDate: Long): ReminderEvent
    data class SetCategory(val category: ReminderCategory): ReminderEvent

    data object ShowNewDialog: ReminderEvent
    data class ShowEditDialog(val item: ReminderItem): ReminderEvent
    data object HideDialog: ReminderEvent

    data class SortItems(val sortType: ReminderSortType): ReminderEvent
}

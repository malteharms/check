package de.malteharms.check.pages.reminder.domain

import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.pages.reminder.data.ReminderSortType
import de.malteharms.check.data.database.tables.ReminderCategory
import java.time.LocalDateTime


sealed interface ReminderEvent {

    data object SaveItem: ReminderEvent
    data class UpdateItem(val itemToUpdate: ReminderItem): ReminderEvent
    data class RemoveItem(val item: ReminderItem): ReminderEvent

    data class SetTitle(val title: String): ReminderEvent
    data class SetDueDate(val dueDate: LocalDateTime): ReminderEvent
    data class SetCategory(val category: ReminderCategory): ReminderEvent

    data class AddNotification(val notification: ReminderNotification): ReminderEvent
    data class RemoveNotification(val notification: ReminderNotification): ReminderEvent

    data object ShowNewDialog: ReminderEvent
    data class ShowEditDialog(val item: ReminderItem): ReminderEvent
    data object HideDialog: ReminderEvent

    data class SortItems(val sortType: ReminderSortType): ReminderEvent
}

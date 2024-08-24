package de.malteharms.pages.reminder.domain

import de.malteharms.database.tables.ReminderItem
import de.malteharms.database.tables.NotificationItem
import de.malteharms.database.tables.ReminderCategory
import de.malteharms.utils.model.DateExt
import java.time.temporal.ChronoUnit


sealed interface ReminderEvent {

    data object SaveItem: ReminderEvent
    data class UpdateItem(val itemToUpdate: de.malteharms.database.tables.ReminderItem):
        ReminderEvent
    data class RemoveItem(val item: de.malteharms.database.tables.ReminderItem): ReminderEvent

    data object MoveFromOrToDetailsScreen: ReminderEvent

    data class SetTitle(val title: String): ReminderEvent
    data class SetDueDate(val dueDate: DateExt): ReminderEvent

    data class SetCategory(val category: de.malteharms.database.tables.ReminderCategory):
        ReminderEvent

    data class AddDummyNotification(val value: Int, val timeUnit: ChronoUnit): ReminderEvent
    data class RemoveNotification(val notification: de.malteharms.database.tables.NotificationItem):
        ReminderEvent

    data object ShowNewDialog: ReminderEvent
    data class ShowEditDialog(val item: de.malteharms.database.tables.ReminderItem): ReminderEvent

    data object HideDialog: ReminderEvent
    data class AddOrRemoveFilterCategory(val filter: de.malteharms.database.tables.ReminderCategory):
        ReminderEvent
}

package de.malteharms.pages.reminder.domain

import de.malteharms.database.tables.NotificationItem
import de.malteharms.database.tables.reminder.ReminderItem
import de.malteharms.utils.model.DateExt
import java.time.temporal.ChronoUnit

sealed interface ReminderEvent {

    // setting any ReminderItem value
    data class SetTitle(val title: String): ReminderEvent
    data class SetDue(val due: DateExt?): ReminderEvent
    data class SetDuration(val duration: DateExt?): ReminderEvent
    data class SetCategory(val id: Long): ReminderEvent
    data class SetIsTodo(val isTodo: Boolean): ReminderEvent

    data class AddNotification(val value: Int, val unit: ChronoUnit): ReminderEvent
    data class AddNotificationDate(val date: DateExt): ReminderEvent
    data class RemoveNotification(val id: NotificationItem): ReminderEvent

    // interaction with editor dialog
    data class OpenEditorDialog(val item: ReminderItem?): ReminderEvent
    data class SaveItem(val id: Long?): ReminderEvent
    data class RemoveItem(val id: Long): ReminderEvent
    data object HideEditorDialog: ReminderEvent
}
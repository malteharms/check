package de.malteharms.check.pages.reminder.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_notifications")
data class ReminderNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderItem: Long,

    val valueBeforeDue: Int,
    val interval: ReminderNotificationInterval,

    val notificationId: Int,
    val notificationDate: Long
)

enum class ReminderNotificationInterval {
    DAYS,
    MONTHS
}

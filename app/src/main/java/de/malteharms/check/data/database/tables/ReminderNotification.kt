package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "reminder_notifications")
data class ReminderNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderItem: Long,

    val valueBeforeDue: Int,
    val interval: ReminderNotificationInterval,

    val notificationId: Int,
    val notificationDate: LocalDateTime     // todo: migrate datatype
)

enum class ReminderNotificationInterval {
    DAYS,
    MONTHS
}

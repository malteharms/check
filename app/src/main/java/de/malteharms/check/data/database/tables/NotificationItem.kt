package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import java.time.LocalDateTime

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val channel: NotificationChannel,
    val connectedItem: Long,

    val valueBeforeDue: Long,
    val interval: ReminderNotificationInterval,

    val notificationId: Int,
    val notificationDate: LocalDateTime
)

enum class ReminderNotificationInterval {
    DAYS,
    MONTHS
}

package de.malteharms.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.utils.model.DateExt
import java.time.temporal.ChronoUnit

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val channel: NotificationChannel,
    val connectedItem: Long,

    val valueBeforeDue: Long,
    val timeUnit: ChronoUnit,

    val notificationId: Int,
    val notificationDate: DateExt
)

enum class NotificationChannel {
    REMINDER
}
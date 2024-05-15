package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "reminder_items")
data class ReminderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val category: ReminderCategory = ReminderCategory.GENERAL,

    val todoRelation: Int? = null,

    // todo: implement migration strategy for LocalDateTime
    val dueDate: LocalDateTime,
    val creationDate: LocalDateTime,
    val lastUpdate: LocalDateTime
)

enum class ReminderCategory {
    GENERAL,
    BIRTHDAY,
    AUTOMATIC_RENEW,
    MANUAL_RENEW,
    IMPORTANT_APPOINTMENT
}
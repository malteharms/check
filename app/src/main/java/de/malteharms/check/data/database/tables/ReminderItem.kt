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

    val todoRelation: Long? = null,
    val birthdayRelation: Long? = null,

    val dueDate: LocalDateTime,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val lastUpdate: LocalDateTime = LocalDateTime.now()
)

enum class ReminderCategory {
    GENERAL,
    BIRTHDAY,
    IMPORTANT_APPOINTMENT
}
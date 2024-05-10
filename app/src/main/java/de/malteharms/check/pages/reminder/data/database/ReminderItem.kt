package de.malteharms.check.pages.reminder.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reminder_items")
data class ReminderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val category: ReminderCategory = ReminderCategory.GENERAL,

    val todoRelation: Int? = null,

    val dueDate: Long,
    val creationDate: Long,
    val lastUpdate: Long
)

enum class ReminderCategory {

    GENERAL,
    BIRTHDAY,
    AUTOMATIC_RENEW,
    MANUAL_RENEW,
    IMPORTANT_APPOINTMENT

}
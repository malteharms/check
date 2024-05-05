package de.malteharms.check.pages.reminder.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reminder_items")
data class ReminderItem(
    val title: String,
    val dueDate: Long,
    val creationDate: Long,
    val lastUpdate: Long,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)

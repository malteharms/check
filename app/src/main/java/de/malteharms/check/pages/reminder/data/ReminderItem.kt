package de.malteharms.check.pages.reminder.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reminder_items")
data class ReminderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val category: ReminderCategory = ReminderCategory.GENERAL,
    val notification: String? = null,

    val todoRelation: Int? = null,

    val dueDate: Long,
    val creationDate: Long,
    val lastUpdate: Long,

    )

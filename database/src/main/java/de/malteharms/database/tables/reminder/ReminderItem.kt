package de.malteharms.database.tables.reminder

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.database.Notificationable
import de.malteharms.utils.model.DateExt

@Entity(tableName = "reminders")
data class ReminderItem (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    // a notification item will be linked to the reminder id

    val title: String,
    val due: DateExt? = null,
    val duration: DateExt? = null,
    // TODO implement location

    val category: Long,
    val isTodo: Boolean,
    // TODO implement priority

    val isEditable: Boolean = true,

    val created: DateExt = DateExt.now(atStartOfDay = false),
    val updated: DateExt = DateExt.now(atStartOfDay = false)
): Notificationable

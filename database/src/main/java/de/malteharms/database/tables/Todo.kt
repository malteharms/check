package de.malteharms.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.database.Notificationable
import de.malteharms.utils.model.Priority
import de.malteharms.utils.model.DateExt

@Entity(tableName = "todos")
data class TodoItem(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val details: String? = null,

    val completed: Boolean = false,

    val group: Long,
    val dueDate: DateExt? = null,
    val priority: Priority,

    val label: Long? = null,

    val subTaskOf: Long? = null,

    val created: DateExt = DateExt.now(),
    val modified: DateExt = DateExt.now()
): Notificationable

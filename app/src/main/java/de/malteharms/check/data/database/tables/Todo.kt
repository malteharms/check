package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.check.data.Priority
import de.malteharms.check.domain.Notificationable
import java.time.LocalDateTime

@Entity(tableName = "todos")
data class TodoItem(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val details: String? = null,

    val completed: Boolean = false,

    val group: Long,
    val dueDate: LocalDateTime? = null,
    val priority: Priority,

    val label: Long? = null,

    val subTaskOf: Long? = null,

    val created: LocalDateTime = LocalDateTime.now(),
    val modified: LocalDateTime = LocalDateTime.now()
): Notificationable

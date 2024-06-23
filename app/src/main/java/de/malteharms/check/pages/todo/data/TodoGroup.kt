package de.malteharms.check.pages.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_groups")
data class TodoGroup(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,
    val color: String
)

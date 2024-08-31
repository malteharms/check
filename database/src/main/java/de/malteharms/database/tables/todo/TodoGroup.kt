package de.malteharms.database.tables.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_groups")
data class TodoGroup(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,
    val color: String
)

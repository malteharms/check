package de.malteharms.database.tables.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TodoPriority (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val color: String
)

package de.malteharms.database.tables.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.utils.logic.getRandomHexString

@Entity(tableName = "todo_label")
data class TodoLabel(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val labelMame: String,
    val color: String = getRandomHexString()
)

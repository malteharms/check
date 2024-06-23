package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.check.data.utils.getRandomHexString

@Entity(tableName = "todo_label")
data class TodoLabel(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val labelMame: String,
    val color: String = getRandomHexString()
)

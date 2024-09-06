package de.malteharms.database.tables.reminder

import androidx.room.PrimaryKey

data class ReminderCategory(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val color: Int,
)

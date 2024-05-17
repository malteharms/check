package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "birthdays")
data class Birthday (

    @PrimaryKey
    val id: Long,

    val name: String,
    val birthday: LocalDateTime
)

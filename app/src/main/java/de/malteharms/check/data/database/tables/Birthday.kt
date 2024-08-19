package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.utils.model.DateExt

@Entity(tableName = "birthdays")
data class Birthday (

    @PrimaryKey
    val id: Long,

    val name: String,
    val birthday: DateExt,

    val ignore: Boolean = false
)

package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "planned_meals")
data class PlannedMealItem(

    @PrimaryKey
    val date: LocalDateTime,
    val meal: Long

)


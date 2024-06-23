package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val recipeLink: String?,
    val imageFile: String?,
    val attendees: List<Long>,
    val labels: List<FoodLabel>,

)

enum class FoodLabel {
    MEAL,
    BREAKFAST,
    FAST,
}

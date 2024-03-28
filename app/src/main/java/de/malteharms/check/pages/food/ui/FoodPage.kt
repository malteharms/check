package de.malteharms.check.pages.food.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.pages.food.data.FOOD_CONTAINER_HEIGHT
import de.malteharms.check.pages.food.data.FOOD_CONTAINER_BOTTOM_SPACING
import de.malteharms.check.pages.food.data.getWeekDays
import de.malteharms.check.pages.food.ui.components.FoodPlanRow

@Composable
fun Food(
    navController: NavController
) {
    Column {
        getWeekDays().forEach { weekday ->

            // Weekday Container
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(FOOD_CONTAINER_HEIGHT.dp)
            ) {
                FoodPlanRow(navController, weekday)
            }

            // a little spacing
            Spacer(modifier = Modifier.height(FOOD_CONTAINER_BOTTOM_SPACING.dp))
        }
    }
}
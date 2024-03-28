package de.malteharms.check.pages.food.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.pages.food.data.FOOD_BOX_PADDING_END
import de.malteharms.check.pages.food.data.FOOD_BOX_WIDTH_IN_PERCENT

@Composable
fun FoodPlanRow(
    navController: NavController,
    weekday: String
) {
    val configuration = LocalConfiguration.current
    val screenWidth: Int = configuration.screenWidthDp
    val foodBoxWidth = screenWidth * FOOD_BOX_WIDTH_IN_PERCENT

    Row (
        modifier = Modifier.fillMaxWidth().padding(end = FOOD_BOX_PADDING_END.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = weekday, color = Color.Gray, fontWeight = FontWeight.ExtraLight)
        FoodLabelContainer(
            navController,
            foodBoxWidth
        )
    }
}

package de.malteharms.check.pages.food.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.pages.food.data.FOOD_BOX_CORNER_RADIUS
import de.malteharms.check.presentation.theme.blue60

@Composable
fun FoodLabelContainer(
    navController: NavController,
    foodBoxWidth: Double
) {
    Box(modifier = Modifier
        .clip(shape = RoundedCornerShape(FOOD_BOX_CORNER_RADIUS.dp))
        .background(blue60)
        .width(foodBoxWidth.dp)
        .fillMaxHeight()
    ) {
        Text(text = "Rührei", modifier = Modifier.align(Alignment.Center))
    }
}

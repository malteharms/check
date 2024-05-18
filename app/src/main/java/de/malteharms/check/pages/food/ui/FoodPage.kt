package de.malteharms.check.pages.food.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.pages.food.data.getWeekDays
import de.malteharms.check.presentation.theme.blue60

@Composable
fun Food(
    navController: NavController
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        getWeekDays().forEach { weekday ->

            Row (modifier = Modifier.fillMaxWidth()){
                Column (
                    modifier = Modifier
                        .width(25.dp)
                        .padding(1.dp)
                        .drawBehind {
                            drawCircle(
                                color = blue60,
                                radius = this.size.minDimension
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "${weekday[0]}")
                    Text(text = "12")
                }
            }

        }
    }
}
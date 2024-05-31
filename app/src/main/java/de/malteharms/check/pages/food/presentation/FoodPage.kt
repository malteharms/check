package de.malteharms.check.pages.food.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.data.database.tables.MealItem
import de.malteharms.check.pages.food.presentation.components.EmptyMealContainer
import de.malteharms.check.pages.food.presentation.components.MealContainer
import de.malteharms.check.presentation.components.TopBar

@Composable
fun Food(
    navController: NavController
) {

    // TODO load current week from viewModel

    val item = MealItem(
        title = "Pfannkuchen",
        recipeLink = "https://github.com",
        imageFile = "01.png",
        attendees = listOf(1L, 2L),
        labels = emptyList()
    )

    val meals: List<Pair<String, MealItem?>> = listOf(
        Pair("Montag", item),
        Pair("Dienstag", item),
        Pair("Mittwoch", null),
        Pair("Donnerstag", item),
        Pair("Freitag", item),
        Pair("Samstag", item),
        Pair("Sonntag", item),
    )

    Scaffold(
        topBar = { TopBar(navController, "Food") }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {

            LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)){
                items(meals) {meal ->

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = meal.first)

                        if (meal.second == null) {
                            EmptyMealContainer(onClick = {})
                        } else {
                            MealContainer(item = meal.second!!)
                        }

                    }

                }
            }

        }

    }
}

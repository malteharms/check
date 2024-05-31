package de.malteharms.check.pages.food.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.malteharms.check.R
import de.malteharms.check.data.database.tables.MealItem

@Composable
fun MealContainer(
    item: MealItem
) {

    // TODO load person information from database

    MealContainerBox {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {

                    // ICON BAR
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // RECIPE
                        val hasLink: Boolean = item.recipeLink != null
                        val recipeColor: Color = if (hasLink) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceContainer
                        Icon(
                            // TODO change icon to link
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = recipeColor
                        )

                        // ATTENDEES
                        LazyRow(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(item.attendees) {
                                Spacer(modifier = Modifier.width(2.dp))

                                // TODO change icon to profile picture
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = null,
                                )
                            }
                        }

                    }

                    // TITLE
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                }
            }

    }

}


@Preview
@Composable
fun MealContainerPreview() {
    val item = MealItem(
        title = "Pfannkuchen",
        recipeLink = "https://github.com",
        imageFile = "01.png",
        attendees = listOf(1L, 2L),
        labels = emptyList()
    )

    MealContainer(item = item)
}
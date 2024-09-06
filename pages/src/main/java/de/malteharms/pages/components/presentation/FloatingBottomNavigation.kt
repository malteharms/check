package de.malteharms.pages.components.presentation

import androidx.compose.foundation.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import de.malteharms.pages.components.data.NavigationItem
import de.malteharms.pages.components.data.getBottomNavigationItems


@Composable
fun FloatingBottomNavigation(
    navController: NavController,
    navigationItems: List<NavigationItem>,
    currentItem: String
) {

    val configuration = LocalConfiguration.current
    val screenWidth: Int = configuration.screenWidthDp
    val navigationWith: Double = screenWidth * 0.8

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.large)
                .width(navigationWith.dp)
                .height(50.dp)
                .background(MaterialTheme.colorScheme.surface),

        ) {
            Row (
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                navigationItems.forEach { item ->

                    var color: Color = MaterialTheme.colorScheme.onSurface
                    var icon: Int = item.icon

                    val isActiveItem: Boolean = item.label == currentItem
                    if (isActiveItem) {
                        color = MaterialTheme.colorScheme.primary
                        icon = item.iconSelected
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(vertical = 15.dp)
                            .clickable(
                                onClick = {
                                    // navigate to the new selected page
                                    navController.navigate(item.route)
                                },
                                indication = null,
                                interactionSource = null
                            )
                    ) {

                        Row() {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color),
                            )

                            if (isActiveItem) {
                                Text(text = item.label)
                            }
                        }

                    }

                } // items
            }
        }

        Spacer(modifier = Modifier.height(45.dp))
    }
}


@Preview
@Composable
fun BottomNavigationPreview() {
    FloatingBottomNavigation( rememberNavController(), getBottomNavigationItems(), "Home")
}
package de.malteharms.check.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import de.malteharms.check.data.NavigationItem
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.platform.LocalConfiguration
import de.malteharms.check.data.getBottomNavigationItems
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import de.malteharms.check.data.NAVIGATION_BAR_BOTTOM_SPACE
import de.malteharms.check.data.NAVIGATION_BAR_INNER_HEIGHT
import de.malteharms.check.data.NAVIGATION_BAR_WIDTH_PADDING


@Composable
fun FloatingBottomNavigation(
    navController: NavController,
    navigationItems: List<NavigationItem>
) {

    val configuration = LocalConfiguration.current
    val screenWidth: Int = configuration.screenWidthDp
    val navigationWith: Double = screenWidth * NAVIGATION_BAR_WIDTH_PADDING

    var selectedIcon by remember { mutableIntStateOf(0) }

    var search by remember {
        mutableStateOf("")
    }

    // BACKGROUND CONTAINER
    Box(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.large)
            .width(navigationWith.dp)
            .height(NAVIGATION_BAR_INNER_HEIGHT.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(5.dp),
    ) {

        // COLUMN DIVIDING ICONS AND SEARCH BAR
        Column{

            CustomTextField(
                value = search,
                placeholderText = "Search anything",
                onValueChange = { newValue -> search = newValue}
            )

        }

    }


    /*
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.large)
                .width(navigationWith.dp)
                .height(NAVIGATION_BAR_INNER_HEIGHT.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),

        ) {
            Row (
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                navigationItems.forEachIndexed { index, item ->

                    var color: Color = MaterialTheme.colorScheme.onSurface
                    var icon: Int = item.icon

                    if (selectedIcon == index) {
                        color = MaterialTheme.colorScheme.primary
                        icon = item.iconSelected
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clickable(
                                onClick = {
                                    // update the inner state to change icon color
                                    selectedIcon = index

                                    // navigate to the new selected page
                                    navController.navigate(item.route)
                                },
                                indication = null,
                                interactionSource = null
                            )
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1.0f)
                                    .padding(top = 10.dp),
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color),
                            ) // Image

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                text = item.label,
                                color = color,
                                textAlign = TextAlign.Center,
                                fontSize = MaterialTheme.typography.labelMedium.fontSize
                            )
                        }

                    }

                } // items
            }
        }

        Spacer(modifier = Modifier.height(NAVIGATION_BAR_BOTTOM_SPACE.dp))
    }

     */

}


@Preview
@Composable
fun BottomNavigationPreview() {
    FloatingBottomNavigation( rememberNavController(), getBottomNavigationItems() )
}
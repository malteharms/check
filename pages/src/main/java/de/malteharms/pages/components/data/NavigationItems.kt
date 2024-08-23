package de.malteharms.pages.components.data

import de.malteharms.pages.R


data class NavigationItem(
    val label : String = "",
    val icon : Int = 0,
    val iconSelected: Int = 0,
    val route : String = ""
)


fun getBottomNavigationItems() : List<NavigationItem> {
    return listOf(
        NavigationItem(
            label = "Home",
            icon = R.drawable.ic_home,
            iconSelected = R.drawable.ic_home_filled,
            route = Screens.HomeRoute.route
        ),
        NavigationItem(
            label = "Costs",
            icon = R.drawable.ic_money,
            iconSelected = R.drawable.ic_money,
            route = Screens.CashRoute.route
        ),
        NavigationItem(
            label = "ToDo",
            icon = R.drawable.ic_check,
            iconSelected = R.drawable.ic_check,
            route = Screens.TodoRoute.route
        ),
        NavigationItem(
            label = "Reminder",
            icon = R.drawable.ic_alarm,
            iconSelected = R.drawable.ic_reminder_filled,
            route = Screens.ReminderRoute.route
        ),
        NavigationItem(
            label = "Food",
            icon = R.drawable.ic_drink,
            iconSelected = R.drawable.ic_drink,
            route = Screens.FoodRoute.route
        ),
    )
}
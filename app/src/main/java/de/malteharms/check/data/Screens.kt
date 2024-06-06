package de.malteharms.check.data

sealed class Screens(val route : String) {
    data object WelcomeRoute : Screens("welcome_route")
    data object LoginRoute : Screens("login_route")
    data object RegisterRoute : Screens("register_route")

    data object SettingsRoute : Screens("settings_route")

    data object HomeRoute : Screens("home_route")
    data object CashRoute : Screens("cash_route")
    data object TodoRoute : Screens("todo_route")

    data object ReminderRoute : Screens("reminder_route")
    data object ReminderDetailsRoute : Screens ("reminder_details_route")

    data object FoodRoute : Screens("food_route")
}

sealed class NestedRoutes(val route : String) {
    data object AuthRoute : NestedRoutes("authentication")
    data object MainRoute : NestedRoutes("main_route")
}
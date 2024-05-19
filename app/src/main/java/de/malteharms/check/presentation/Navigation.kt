package de.malteharms.check.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import de.malteharms.check.data.NestedRoutes
import de.malteharms.check.data.Screens
import de.malteharms.check.pages.profile.ui.ProfilePage
import de.malteharms.check.pages.cash.ui.Cash
import de.malteharms.check.pages.food.ui.Food
import de.malteharms.check.pages.home.ui.Home
import de.malteharms.check.pages.reminder.presentation.ReminderDetailsPage
import de.malteharms.check.pages.reminder.presentation.ReminderPage
import de.malteharms.check.pages.reminder.presentation.ReminderViewModel
import de.malteharms.check.pages.settings.data.SettingsState
import de.malteharms.check.pages.settings.presentation.SettingsPage
import de.malteharms.check.pages.settings.presentation.SettingsViewModel
import de.malteharms.check.pages.todo.ui.Todo
import de.malteharms.check.pages.welcome.ui.Welcome

@Composable
fun Navigation(
    navController: NavHostController,
    reminderViewModel: ReminderViewModel,
    settingsViewModel: SettingsViewModel
) {
    val reminderState by reminderViewModel.state.collectAsState()
    val settingsState: SettingsState by settingsViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = NestedRoutes.MainRoute.route) {

        // navigation graph for authentication procedure
        navigation(
            startDestination = Screens.WelcomeRoute.route,
            route = NestedRoutes.AuthRoute.route
        ) {
            composable(Screens.WelcomeRoute.route) { Welcome(navController) }
            composable(Screens.LoginRoute.route) {  }
            composable(Screens.RegisterRoute.route) {  }
        }

        // navigation graph for main app usage
        navigation(
            startDestination = Screens.ReminderRoute.route,
            route = NestedRoutes.MainRoute.route
        ) {
            // general pages available in main route
            composable(Screens.ProfileRoute.route) { ProfilePage(navController) }
            composable(Screens.SettingsRoute.route) {
                SettingsPage(
                    navController = navController,
                    state = settingsState,
                    onEvent = settingsViewModel::onEvent
                )
            }

            // pages available through bottom navigation
            composable(Screens.HomeRoute.route) { Home(navController) }
            composable(Screens.CashRoute.route) { Cash(navController = navController) }
            composable(Screens.TodoRoute.route) { Todo(navController = navController) }

            composable(Screens.ReminderRoute.route) {
                ReminderPage(
                    navController = navController,
                    state = reminderState,
                    getNotifications = reminderViewModel::getNotifications,
                    onEvent = reminderViewModel::onEvent
                )
            }

            composable(Screens.ReminderDetailsRoute.route) {
                ReminderDetailsPage(
                    navController = navController,
                    state = reminderState,
                    onEvent = reminderViewModel::onEvent,
                    getNotifications = reminderViewModel::getNotifications
                )
            }

            composable(Screens.FoodRoute.route) { Food(navController = navController) }

        }
    }
}

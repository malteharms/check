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
import de.malteharms.check.pages.cash.ui.Cash
import de.malteharms.check.pages.food.presentation.Food
import de.malteharms.check.pages.home.ui.Home
import de.malteharms.check.pages.login.presentation.LoginPage
import de.malteharms.check.pages.register.presentation.RegisterPage
import de.malteharms.check.pages.reminder.data.ReminderState
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
    utilityViewModel: UtilityViewModel,
    reminderViewModel: ReminderViewModel,
    settingsViewModel: SettingsViewModel
) {
    val reminderState: ReminderState by reminderViewModel.state.collectAsState()
    val settingsState: SettingsState by settingsViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = NestedRoutes.AuthRoute.route) {

        // navigation graph for authentication procedure
        navigation(
            startDestination = Screens.RegisterRoute.route,
            route = NestedRoutes.AuthRoute.route
        ) {
            composable(Screens.WelcomeRoute.route) { Welcome(navController) }
            composable(Screens.LoginRoute.route) { LoginPage(navController) }
            composable(Screens.RegisterRoute.route) { RegisterPage(navController) }
        }

        // navigation graph for main app usage
        navigation(
            startDestination = Screens.HomeRoute.route,
            route = NestedRoutes.MainRoute.route
        ) {
            // general pages available in main route
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
                    hasNotifications = reminderViewModel::hasNotifications,
                    onEvent = reminderViewModel::onEvent,
                    syncContacts = utilityViewModel::syncBirthdaysFromContacts
                )
            }

            composable(Screens.ReminderDetailsRoute.route) {
                ReminderDetailsPage(
                    navController = navController,
                    state = reminderState,
                    onEvent = reminderViewModel::onEvent,
                    hasNotifications = reminderViewModel::hasNotifications
                )
            }

            composable(Screens.FoodRoute.route) { Food(navController = navController) }

        }
    }
}

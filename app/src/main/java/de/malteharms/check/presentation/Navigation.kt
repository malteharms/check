package de.malteharms.check.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import de.malteharms.pages.auth.AuthState
import de.malteharms.pages.auth.AuthViewModel
import de.malteharms.pages.ui.Cash
import de.malteharms.pages.food.presentation.Food
import de.malteharms.pages.auth.login.presentation.LoginPage
import de.malteharms.pages.auth.register.presentation.RegisterPage
import de.malteharms.pages.todo.presentation.Todo
import de.malteharms.pages.todo.data.TodoState
import de.malteharms.pages.todo.presentation.TodoViewModel
import de.malteharms.pages.auth.welcome.presentation.Welcome
import de.malteharms.pages.components.data.NestedRoutes
import de.malteharms.pages.components.data.Screens
import de.malteharms.pages.home.presentation.Home

@Composable
fun Navigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    utilityViewModel: UtilityViewModel,
    todoViewModel: TodoViewModel,
) {
    val authState: AuthState by authViewModel.state.collectAsState()
    val todoState: TodoState by todoViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = NestedRoutes.MainRoute.route) {

        // navigation graph for authentication procedure
        navigation(
            startDestination = Screens.RegisterRoute.route,
            route = NestedRoutes.AuthRoute.route
        ) {
            composable(Screens.WelcomeRoute.route) { Welcome(navController) }
            composable(Screens.LoginRoute.route) { LoginPage(navController) }
            composable(Screens.RegisterRoute.route) {
                RegisterPage(
                    navController = navController,
                    state = authState,
                    onEvent = authViewModel::onEvent
                )
            }
        }

        // navigation graph for main app usage
        navigation(
            startDestination = Screens.HomeRoute.route,
            route = NestedRoutes.MainRoute.route
        ) {
            // general pages available in main route
            composable(Screens.SettingsRoute.route) {  }

            // pages available through bottom navigation
            composable(Screens.HomeRoute.route) { Home(navController) }
            composable(Screens.CashRoute.route) { Cash(navController = navController) }
            composable(Screens.TodoRoute.route) {
                Todo(
                    navController = navController,
                    state = todoState,
                    onEvent = todoViewModel::onEvent
                )
            }

            composable(Screens.ReminderRoute.route) {}
            composable(Screens.FoodRoute.route) { Food(navController = navController) }

        }
    }
}

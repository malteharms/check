package de.malteharms.check

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.compose.ui.Modifier
import de.malteharms.check.presentation.Navigation
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import de.malteharms.check.presentation.theme.CheckTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import de.malteharms.check.data.getBottomNavigationItems
import de.malteharms.check.data.notification.NotificationHandler
import de.malteharms.check.pages.reminder.presentation.ReminderViewModel
import de.malteharms.check.pages.settings.presentation.SettingsViewModel
import de.malteharms.check.presentation.UtilityViewModel
import de.malteharms.check.presentation.components.FloatingBottomNavigation
import de.malteharms.check.presentation.viewModelFactory


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this will set the size of the application to the whole screen
        // otherwise the page would be interrupted on the bottom of the screen
        // where the navigation slider is located
        enableEdgeToEdge()

        setContent {
            CheckTheme {

                val utilityViewModel: UtilityViewModel = viewModel<UtilityViewModel>()

                /*
                * The SettingsViewModel will work as a bridge between the Settings UI and
                * the database, where all settings are stored.
                * The Settings need to be loaded at first to guarantee, that other
                * functions and ViewModels can load the settings from the database
                 */
                val settingsViewModel: SettingsViewModel = viewModel<SettingsViewModel>(
                    factory = viewModelFactory { SettingsViewModel(
                        dao = CheckApp.appModule.db.itemDao(),
                        syncContacts = utilityViewModel::syncBirthdaysFromContacts
                    ) }
                )

                // try to load all settings from database
                settingsViewModel.loadSettingsFromDatabase()

                /*
                * The ReminderViewModel will provide the state for showing the ReminderPage.
                * A user has the possibility to load birthdays from contacts. To always stay
                * up to date, the birthdays will be synced when starting the application
                */
                val reminderViewModel: ReminderViewModel = viewModel<ReminderViewModel>(
                    factory = viewModelFactory { ReminderViewModel(
                        app = CheckApp.appModule
                    ) }
                )

                // TODO #8
                //  add possibility to ignore birthdays from contacts

                utilityViewModel.syncBirthdaysFromContacts()

                // if notification permission are granted, schedule all notifications
                // on startup
                var notificationPermission: PermissionState? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermission = rememberPermissionState(
                        permission = Manifest.permission.POST_NOTIFICATIONS
                    )
                }

                // update overdue NOTIFICATIONS
                NotificationHandler.updateOverdueNotifications(
                    dao = CheckApp.appModule.db.itemDao()
                )

                // reschedule notification if necessary
                val hasNotificationPermission: Boolean =
                    notificationPermission?.status?.isGranted ?: true

                if (hasNotificationPermission) {
                    NotificationHandler.rescheduleAllNotifications(
                        dao = CheckApp.appModule.db.itemDao(),
                        notificationScheduler = CheckApp.appModule.notificationScheduler
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = { FloatingBottomNavigation( navController, getBottomNavigationItems() ) },
                        modifier = Modifier.fillMaxSize()
                    ) {  innerPadding ->
                        Box(modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .fillMaxSize()
                        ) {
                            Navigation(
                                navController,
                                utilityViewModel,
                                reminderViewModel,
                                settingsViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

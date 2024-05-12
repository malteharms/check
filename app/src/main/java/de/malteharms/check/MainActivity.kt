package de.malteharms.check

import android.os.Bundle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import de.malteharms.check.ui.Navigation
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import de.malteharms.check.ui.theme.CheckTheme
import androidx.compose.material3.MaterialTheme
import de.malteharms.check.ui.components.TopBar
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import de.malteharms.check.data.SCREEN_INNER_PADDING
import de.malteharms.check.data.database.CheckDatabase
import de.malteharms.check.data.getBottomNavigationItems
import de.malteharms.check.data.notification.AndroidAlarmScheduler
import de.malteharms.check.pages.reminder.presentation.ReminderViewModel
import de.malteharms.check.ui.components.FloatingBottomNavigation
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets


class MainActivity : ComponentActivity() {

    private val client = HttpClient(CIO) {
        install(Logging)
        install(WebSockets)
    }

    private val db by lazy {
        CheckDatabase.getDatabase(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // handler for schedule notifications
        val notificationScheduler = AndroidAlarmScheduler(this)

        // viewmodel for reminder page
        val reminderViewModel by viewModels<ReminderViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ReminderViewModel(db.itemDao(), notificationScheduler) as T
                    }
                }
            }
        )

        // UI Begin
        enableEdgeToEdge()

        setContent {
            CheckTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        topBar = { TopBar() },
                        bottomBar = { FloatingBottomNavigation( navController, getBottomNavigationItems() ) },
                        modifier = Modifier.fillMaxSize()
                    ) {  innerPadding ->
                        Box(modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .fillMaxSize()
                            .padding(SCREEN_INNER_PADDING.dp)
                        ) {
                            Navigation(
                                navController,
                                reminderViewModel
                            )
                        }
                    }
                }
            }
        }
    }

}

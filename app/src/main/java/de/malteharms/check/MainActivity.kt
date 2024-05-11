package de.malteharms.check


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import de.malteharms.check.data.NOTIFICATION_ID
import de.malteharms.check.data.NOTIFICATION_MESSAGE
import de.malteharms.check.data.NOTIFICATION_TITLE
import de.malteharms.check.data.SCREEN_INNER_PADDING
import de.malteharms.check.data.database.CheckDatabase
import de.malteharms.check.data.getBottomNavigationItems
import de.malteharms.check.data.internal.NotificationResult
import de.malteharms.check.data.internal.NotificationState
import de.malteharms.check.data.notification.NotificationChannel
import de.malteharms.check.data.notification.ReminderNotificationReceiver
import de.malteharms.check.pages.reminder.presentation.ReminderViewModel
import de.malteharms.check.ui.components.FloatingBottomNavigation
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import kotlin.random.Random


class MainActivity : ComponentActivity() {

    private val client = HttpClient(CIO) {
        install(Logging)
        install(WebSockets)
    }

    private val db by lazy {
        CheckDatabase.getDatabase(this)
    }

    private val reminderViewModel by viewModels<ReminderViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ReminderViewModel(
                        db.itemDao(),
                        ::scheduleNotification
                    ) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    fun scheduleNotification(
        channel: NotificationChannel,
        title: String,
        message: String,
        schedule: Long
        ): NotificationResult {

        // todo check if permission is already given

        val intent = Intent(
            applicationContext,
            when (channel) {
                NotificationChannel.REMINDER -> ReminderNotificationReceiver::class.java
            }
        )

        val nId: Int = Random.nextInt(0, Int.MAX_VALUE)

        intent.putExtra(NOTIFICATION_ID, nId)
        intent.putExtra(NOTIFICATION_TITLE, title)
        intent.putExtra(NOTIFICATION_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            nId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            schedule,
            pendingIntent
        )

        return NotificationResult(NotificationState.SUCCESS, nId)
    }
}

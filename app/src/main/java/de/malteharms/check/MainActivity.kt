package de.malteharms.check

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import de.malteharms.check.ui.Navigation
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import de.malteharms.check.ui.theme.CheckTheme
import androidx.compose.material3.MaterialTheme
import de.malteharms.check.ui.components.TopBar
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import de.malteharms.check.data.SCREEN_INNER_PADDING
import de.malteharms.check.data.database.CheckDatabase
import de.malteharms.check.data.getBottomNavigationItems
import de.malteharms.check.data.notification.Notification
import de.malteharms.check.data.notification.channelID
import de.malteharms.check.data.notification.messageExtra
import de.malteharms.check.data.notification.notificationID
import de.malteharms.check.data.notification.titleExtra
import de.malteharms.check.pages.reminder.presentation.ReminderViewModel
import de.malteharms.check.ui.components.FloatingBottomNavigation
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import java.util.Calendar


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
                    return ReminderViewModel(db.itemDao()) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel()
        if (checkNotificationPermissions(this)) {
            // Schedule a notification
            scheduleNotification()
        }

        setContent {
            CheckTheme() {
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

    private fun createNotificationChannel() {
        // Create a notification channel for devices running
        // Android Oreo (API level 26) and above
        val name = "Notify Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        // Get the NotificationManager service and create the channel
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun checkNotificationPermissions(context: Context): Boolean {
        // Check if notification permissions are granted
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val isEnabled = notificationManager.areNotificationsEnabled()

        if (!isEnabled) {
            // Open the app notification settings if notifications are not enabled
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            context.startActivity(intent)

            return false
        }

        // Permissions are granted
        return true
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification() {
        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(applicationContext, Notification::class.java)

        // Extract title and message from user input
        val title = "Check App"
        val message = "Hello World"

        // Add title and message as extras to the intent
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the AlarmManager service
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Get the selected time and schedule the notification
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        Log.i("Notification", "Notification is scheduled")
    }

    private fun getTime(): Long {
        // Get selected time from TimePicker and DatePicker
        val minute = 16
        val hour = 11
        val day = 11
        val month = 4
        val year = 2024

        // Create a Calendar instance and set the selected date and time
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)

        Log.i("Notification", "Time set to ${calendar.time}")

        return calendar.timeInMillis
    }
}

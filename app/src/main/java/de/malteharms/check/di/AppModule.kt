package de.malteharms.check.di

import android.Manifest
import android.content.Context
import android.util.Log
import com.google.accompanist.permissions.rememberPermissionState
import de.malteharms.check.data.database.CheckDatabase
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.data.notification.AndroidAlarmScheduler
import de.malteharms.check.data.notification.dataclasses.AlarmItem
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import de.malteharms.check.data.provider.ContactsProvider
import de.malteharms.check.domain.AlarmScheduler
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.pages.reminder.presentation.getTextForDurationInDays
import de.malteharms.check.pages.settings.data.ReminderSettings
import de.malteharms.check.pages.settings.data.SettingValue
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets


interface AppModule{
    val db: CheckDatabase
    val websocketClient: HttpClient
    val notificationScheduler: AlarmScheduler
    val contactsProvider: ContactsProvider

    fun loadNotifications()
}

class AppModuleImpl(
    context: Context
) : AppModule {

    companion object {
        val TAG: String? = AppModuleImpl::class.simpleName
    }

    override val db by lazy {
        CheckDatabase.getDatabase(context)
    }

    override val websocketClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
        }
    }

    override val notificationScheduler by lazy {
        AndroidAlarmScheduler(context)
    }

    override val contactsProvider: ContactsProvider by lazy {
        ContactsProvider(context)
    }

    override fun loadNotifications() {
        val dao: CheckDao = db.itemDao()

        // schedule notifications
        val notifications: List<ReminderNotification> = dao.getAllNotifications()

        notifications.forEach { notification ->
            val reminderItem: ReminderItem = dao.getReminderItemById(notification.reminderItem)

            notificationScheduler.schedule(
                notificationId = notification.notificationId,
                item = AlarmItem(
                    channel = NotificationChannel.REMINDER,
                    time = notification.notificationDate,
                    title = "Reminder >> ${reminderItem.title}",
                    message = getTextForDurationInDays(reminderItem.dueDate)
                )
            )
        }

        Log.i(TAG, "Loaded and re-scheduled all notifications")
    }

}

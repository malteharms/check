package de.malteharms.notification.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import de.malteharms.database.tables.NotificationChannel
import de.malteharms.notification.data.dataclasses.NotificationResult
import de.malteharms.notification.data.dataclasses.NotificationState
import de.malteharms.notification.data.dataclasses.AlarmItem
import de.malteharms.notification.domain.AlarmScheduler
import kotlin.random.Random


const val NOTIFICATION_ID = "idExtra"
const val NOTIFICATION_TITLE = "titleExtra"
const val NOTIFICATION_MESSAGE = "messageExtra"

const val NOTIFICATION_REMINDER_CHANNEL_ID = "notify-reminder"

class AndroidAlarmScheduler(
    private val context: Context
): AlarmScheduler {

    companion object {
        private val TAG: String? = AndroidAlarmScheduler::class.simpleName
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(notificationId: Int?, item: AlarmItem): NotificationResult {
        val nId: Int = notificationId ?: Random.nextInt(1, Int.MAX_VALUE)

        val intent: Intent = when (item.channel) {
            NotificationChannel.REMINDER -> Intent(context, ReminderNotificationReceiver::class.java)
                .putExtra(NOTIFICATION_TITLE, item.title)
                .putExtra(NOTIFICATION_MESSAGE, item.message)
                .putExtra(NOTIFICATION_ID, nId)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, nId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            item.time.toTimestamp() * 1000,
            pendingIntent
        )

        Log.i(TAG, "Scheduled notification with ID $nId at ${item.time} for title ${item.title}")
        return NotificationResult(NotificationState.SUCCESS, nId)
    }

    override fun cancel(nId: Int): NotificationResult {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context, nId,
                Intent(context, ReminderNotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        Log.i(TAG, "Canceled notification with ID $nId from operating system")
        return NotificationResult(NotificationState.SUCCESS, nId)
    }

}
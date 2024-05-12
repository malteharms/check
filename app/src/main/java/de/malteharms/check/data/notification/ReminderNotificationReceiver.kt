package de.malteharms.check.data.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import de.malteharms.check.R
import de.malteharms.check.data.NOTIFICATION_ID
import de.malteharms.check.data.NOTIFICATION_MESSAGE
import de.malteharms.check.data.NOTIFICATION_REMINDER_CHANNEL_ID
import de.malteharms.check.data.NOTIFICATION_TITLE


class ReminderNotificationReceiver : BroadcastReceiver() {

    companion object {
        private val TAG: String? = ReminderNotificationReceiver::class.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_check)
            .setContentTitle(intent.getStringExtra(NOTIFICATION_TITLE))
            .setContentText(intent.getStringExtra(NOTIFICATION_MESSAGE))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        intent.getIntExtra(NOTIFICATION_ID, 0).let {
            manager.notify(it, notification)
            Log.i(TAG, "Notified notification ID $it")
        }
    }
}

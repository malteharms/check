package de.malteharms.check

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import de.malteharms.check.data.NOTIFICATION_REMINDER_CHANNEL_ID

class CheckApp: Application() {

    companion object {
        val TAG: String? = CheckApp::class.simpleName
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val reminderChannel = NotificationChannel(
            NOTIFICATION_REMINDER_CHANNEL_ID,
            "Reminder",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        reminderChannel.description = "Used to send reminder"

        notificationManager.createNotificationChannel(reminderChannel)
        Log.i(TAG, "Created ${reminderChannel.id} notification channel")
    }

}
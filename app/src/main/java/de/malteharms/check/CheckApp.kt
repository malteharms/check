package de.malteharms.check

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import de.malteharms.check.di.AppModule
import de.malteharms.check.di.AppModuleImpl
import de.malteharms.notification.data.NOTIFICATION_REMINDER_CHANNEL_ID

class CheckApp: Application() {

    companion object {
        val TAG: String? = CheckApp::class.simpleName
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(context = this)

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
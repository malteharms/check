package de.malteharms.check.di

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.malteharms.check.R
import de.malteharms.check.data.NOTIFICATION_REMINDER_CHANNEL_ID
import de.malteharms.check.data.notification.ReminderNotificationReceiver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {

        val notificationManager = NotificationManagerCompat.from(context)
        val reminderChannel = NotificationChannel(
            NOTIFICATION_REMINDER_CHANNEL_ID,
            "Reminder Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(reminderChannel)

        return notificationManager
    }

    @Singleton
    @Provides
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    fun provideReminderNotificationIntent(
        @ApplicationContext context: Context
    ): Intent {
        return Intent(context, ReminderNotificationReceiver::class.java)
    }

}
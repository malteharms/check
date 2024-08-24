package de.malteharms.notification.data

import android.util.Log
import de.malteharms.notification.data.dataclasses.NotificationResult
import de.malteharms.notification.data.dataclasses.NotificationState
import de.malteharms.notification.data.dataclasses.AlarmItem
import de.malteharms.notification.domain.AlarmScheduler
import de.malteharms.database.Notificationable
import de.malteharms.database.tables.NotificationChannel
import de.malteharms.database.tables.NotificationItem
import de.malteharms.database.tables.ReminderItem
import de.malteharms.utils.logic.timeBetween
import de.malteharms.utils.model.DateExt
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

class NotificationHandler {

    companion object {
        private val TAG: String? = NotificationHandler::class.simpleName

        fun scheduleNotification(
            alarmScheduler: AlarmScheduler,
            type: NotificationChannel,
            connectedItem: Notificationable,
            notificationDate: DateExt,
            notificationId: Int? = null
        ): NotificationItem? {
            return when (type) {
                NotificationChannel.REMINDER -> scheduleReminderNotification(
                    alarmScheduler, connectedItem as ReminderItem, notificationDate, notificationId
                )
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun updateNotifications(
            dao: de.malteharms.database.CheckDao,
            alarmScheduler: AlarmScheduler,
            hasPermission: Boolean = false
        ) {
            val currentTimestamp: Long = DateExt.now().toTimestamp()

            // get all notifications, which are have an overdue notification date
            // also update those notifications, which have the notification date today
            val overdueNotifications: List<NotificationItem> = dao.getOverdueNotifications(
                timestamp = currentTimestamp
            )

            var updatedNotifications = 0

            GlobalScope.launch {
                overdueNotifications.forEach { notificationItem ->

                    // remove the existing notification
                    dao.removeNotification(notificationItem)

                    // from here, the app needs permissions to reschedule
                    // notifications
                    if (!hasPermission) {
                        return@forEach
                    }

                    // reschedule
                    val connectedItem: Notificationable = when (notificationItem.channel) {
                        NotificationChannel.REMINDER -> {
                            dao.getReminderItemById(notificationItem.connectedItem)
                        }
                    } ?: return@forEach

                    when (connectedItem) {
                        is ReminderItem -> {
                            if (connectedItem.category != de.malteharms.database.tables.ReminderCategory.BIRTHDAY) {
                                return@forEach
                            }
                        }
                        else -> return@forEach
                    }

                    val newNotification: NotificationItem = scheduleNotification(
                        alarmScheduler = alarmScheduler,
                        type = notificationItem.channel,
                        connectedItem = connectedItem,
                        notificationDate = notificationItem.notificationDate.applyNextYear()
                    ) ?: return@forEach

                    // update database
                    dao.insertNotification(newNotification)
                    updatedNotifications += 1
                }
            }

            Log.i(TAG, "Rescheduled $updatedNotifications notifications")
        }

        private fun scheduleReminderNotification(
            alarmScheduler: AlarmScheduler,
            reminderItem: de.malteharms.database.tables.ReminderItem,
            notificationDate: DateExt,
            notificationId: Int?
        ): de.malteharms.database.tables.NotificationItem? {

            val alarmItem = AlarmItem(
                channel = NotificationChannel.REMINDER,
                time = notificationDate,
                title = reminderItem.title,
                message = reminderItem.dueDate.toStringUntilDue(notificationDate)
            )

            val schedulingResult: NotificationResult = alarmScheduler.schedule(
                notificationId = notificationId,
                item = alarmItem
            )

            val timePeriod = timeBetween(
                end = reminderItem.dueDate,
                start = notificationDate
            )

            return when (schedulingResult.state) {
                NotificationState.SUCCESS -> {
                    de.malteharms.database.tables.NotificationItem(
                        connectedItem = reminderItem.id,
                        channel = NotificationChannel.REMINDER,
                        notificationId = schedulingResult.notificationId,
                        notificationDate = notificationDate,
                        valueBeforeDue = timePeriod.let {
                            when {
                                it.months > 0 -> it.months
                                else -> it.days
                            }
                        },
                        timeUnit = timePeriod.let {
                            when {
                                it.months > 0 -> ChronoUnit.MONTHS
                                else -> ChronoUnit.DAYS
                            }
                        }
                    )
                }

                NotificationState.NOTIFICATION_IS_DISABLED -> {
                    Log.e(TAG, "Could not schedule notification due to missing permission!")
                    null
                }
            }
        }
    }
}
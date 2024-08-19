package de.malteharms.check.data.notification

import android.util.Log
import de.malteharms.check.data.notification.dataclasses.NotificationResult
import de.malteharms.check.data.notification.dataclasses.NotificationState
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.NotificationItem
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.notification.dataclasses.AlarmItem
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import de.malteharms.check.domain.AlarmScheduler
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.domain.Notificationable
import de.malteharms.check.pages.reminder.presentation.getTextForDurationInDays
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
            dao: CheckDao,
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
                            if (connectedItem.category != ReminderCategory.BIRTHDAY) {
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
            reminderItem: ReminderItem,
            notificationDate: DateExt,
            notificationId: Int?
        ): NotificationItem? {

            val alarmItem = AlarmItem(
                channel = NotificationChannel.REMINDER,
                time = notificationDate,
                title = reminderItem.title,
                message = getTextForDurationInDays(
                    due = reminderItem.dueDate,
                    today = notificationDate
                )
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
                    NotificationItem(
                        connectedItem = reminderItem.id,
                        channel = NotificationChannel.REMINDER,
                        notificationId = schedulingResult.notificationId,
                        notificationDate = notificationDate,
                        valueBeforeDue = timePeriod.let { when {
                            it.months > 0 -> it.months
                            else -> it.days
                        } },
                        timeUnit = timePeriod.let { when {
                            it.months > 0 -> ChronoUnit.MONTHS
                            else -> ChronoUnit.DAYS
                        } }
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
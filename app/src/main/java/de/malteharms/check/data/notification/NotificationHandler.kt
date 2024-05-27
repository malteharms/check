package de.malteharms.check.data.notification

import android.util.Log
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.data.notification.dataclasses.AlarmItem
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import de.malteharms.check.domain.AlarmScheduler
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.pages.reminder.data.calculateCorrectYearOfNextBirthday
import de.malteharms.check.pages.reminder.data.calculateNotificationDate
import de.malteharms.check.pages.reminder.presentation.getTextForDurationInDays
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class NotificationHandler {

    companion object {
        private val TAG: String? = NotificationHandler::class.simpleName

        @OptIn(DelicateCoroutinesApi::class)
        fun updateOverdueNotifications(dao: CheckDao) {
            val notifications: List<ReminderNotification> = dao.getAllNotifications()

            notifications.forEach{ notification ->
                val reminderItem: ReminderItem? = dao.getReminderItemById(notification.reminderItem)

                if (notification.notificationDate.isBefore(LocalDate.now().atStartOfDay())) {
                    GlobalScope.launch {
                        // remove existing notification, which is overdue
                        dao.removeReminderNotification(notification)

                        if (reminderItem == null) {
                            Log.w(TAG, "Cannot load reminder item for notification ${notification.id}")
                            return@launch
                        }

                        if (reminderItem.category == ReminderCategory.BIRTHDAY) {
                            val updatedYear: Int = calculateCorrectYearOfNextBirthday(reminderItem.dueDate)
                            val updatedReminderItem = reminderItem.copy(
                                dueDate = reminderItem.dueDate.withYear(updatedYear),
                                lastUpdate = LocalDateTime.now()
                            )

                            val notificationDate: LocalDateTime = calculateNotificationDate(
                                dueDate = updatedReminderItem.dueDate,
                                valueForNotification = notification.valueBeforeDue,
                                daysOrMonths = notification.interval
                            )

                            dao.updateReminderItem(updatedReminderItem)
                            dao.insertReminderNotification(ReminderNotification(
                                reminderItem = reminderItem.id,
                                valueBeforeDue = notification.valueBeforeDue,
                                interval = notification.interval,
                                notificationId = notification.notificationId,
                                notificationDate = notificationDate
                            ))
                        }
                    }
                }

            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun rescheduleAllNotifications(
            dao: CheckDao,
            notificationScheduler: AlarmScheduler
        ) {
            // schedule notifications
            val notifications: List<ReminderNotification> = dao.getAllNotifications()

            GlobalScope.launch {
                notifications.forEach { notification ->
                    val reminderItem: ReminderItem? =
                        dao.getReminderItemById(notification.reminderItem)

                    if (reminderItem == null) {
                        Log.w(TAG, "Cannot load reminder item for notification ${notification.id}")
                        return@forEach
                    }

                    notificationScheduler.schedule(
                        notificationId = notification.notificationId,
                        item = AlarmItem(
                            channel = NotificationChannel.REMINDER,
                            time = notification.notificationDate,
                            title = reminderItem.title,
                            message = getTextForDurationInDays(reminderItem.dueDate)
                        )
                    )

                    Log.i(
                        TAG,
                        "Scheduled reminder item ${reminderItem.id} at ${notification.notificationDate}"
                    )

                }
            }
        }
    }
}
package de.malteharms.check.pages.reminder.presentation

import de.malteharms.check.data.TimePeriod
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.NotificationItem
import de.malteharms.check.data.database.tables.ReminderNotificationInterval
import de.malteharms.check.pages.reminder.data.timeBetween
import java.time.LocalDate
import java.time.LocalDateTime


fun getTextForDurationInDays(
    due: LocalDateTime,
    today: LocalDateTime = LocalDate.now().atStartOfDay()
): String {
    val periodBetweenTodayAndDue: TimePeriod = timeBetween(end = due, start = today)

    val dayText: String

    // handle values for years
    if (periodBetweenTodayAndDue.years > 0 || periodBetweenTodayAndDue.years < 0) {
        dayText = when (periodBetweenTodayAndDue.years) {
            in -1L .. 1L -> "${periodBetweenTodayAndDue.years}\nJahr"
            else -> "${periodBetweenTodayAndDue.years}\nJahre"
        }
    }

    else if (periodBetweenTodayAndDue.months > 0 || periodBetweenTodayAndDue.months < 0) {
        dayText = when (periodBetweenTodayAndDue.months) {
            in -1L .. 1L -> "${periodBetweenTodayAndDue.months}\nMonat"
            else -> "${periodBetweenTodayAndDue.months}\nMonate"
        }
    }

    else {
        dayText = when (periodBetweenTodayAndDue.days) {
            0L -> "Heute"
            -1L -> "${periodBetweenTodayAndDue.days}\nTag"
            1L-> "${periodBetweenTodayAndDue.days}\nTag"
            else -> "${periodBetweenTodayAndDue.days}\nTage"
        }
    }

    return dayText.trim()
}

fun getCategoryRepresentation(category: ReminderCategory): String {
    return when(category) {
        ReminderCategory.GENERAL -> "Allgemein"
        ReminderCategory.BIRTHDAY -> "Geburtstag"
        ReminderCategory.IMPORTANT_APPOINTMENT -> "Wichtiger Termin"
    }
}

fun getNotificationText(
    notification: NotificationItem
): String {

    if (notification.valueBeforeDue == 0L)
        return "Am selben Tag"

    val intervalText: String = when (notification.interval) {
        ReminderNotificationInterval.DAYS -> if (notification.valueBeforeDue == 1L) "Tag" else "Tage"
        ReminderNotificationInterval.MONTHS -> if (notification.valueBeforeDue == 1L) "Monat" else "Monate"
    }
    return "${notification.valueBeforeDue} $intervalText vorher"
}

fun getNotificationIntervalRepresentation(interval: ReminderNotificationInterval): String {
    return when(interval) {
        ReminderNotificationInterval.DAYS -> "Tage"
        ReminderNotificationInterval.MONTHS -> "Monate"
    }
}

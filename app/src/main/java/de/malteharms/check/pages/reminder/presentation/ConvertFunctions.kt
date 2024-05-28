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
    val periodBetweenTodayAndDue: TimePeriod = timeBetween(dateToReach = due, today = today)

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

fun getNotificationText(notification: NotificationItem): String {
    val period: TimePeriod = timeBetween(dateToReach = notification.notificationDate)

    if (period.days == 0L && period.months == 0L && period.years == 0L)
        return "Am selben Tag"

    val value: Int = when {
        period.months == 0L && period.years == 0L -> period.days.toInt()
        period.years == 0L -> period.months.toInt()
        else -> period.years.toInt()
    }

    val interval = when {
        period.months == 0L && period.years == 0L -> if(period.days == 1L) { "Tag" } else "Tage"
        period.years == 0L -> if(period.months == 1L) { "Monat" } else "Monate"
        else -> if (period.years == 1L) "Jahr" else "Jahre"
    }

    return "$value $interval vorher"
}

fun getNotificationIntervalRepresentation(interval: ReminderNotificationInterval): String {
    return when(interval) {
        ReminderNotificationInterval.DAYS -> "Tage"
        ReminderNotificationInterval.MONTHS -> "Monate"
    }
}

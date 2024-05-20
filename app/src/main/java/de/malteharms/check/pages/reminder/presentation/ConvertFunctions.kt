package de.malteharms.check.pages.reminder.presentation

import de.malteharms.check.data.TimePeriod
import de.malteharms.check.pages.reminder.data.ReminderSortType
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.data.database.tables.ReminderNotificationInterval
import de.malteharms.check.pages.reminder.data.timeBetween
import java.time.LocalDateTime


fun getTextForDurationInDays(due: LocalDateTime): String {
    val periodBetweenTodayAndDue: TimePeriod = timeBetween(dateToReach = due)

    val prefix: String
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
            in -1L .. 1L -> "${periodBetweenTodayAndDue.days}\nTag"
            else -> "${periodBetweenTodayAndDue.days}\nTage"
        }
    }

    return dayText.trim()
}

fun getAddOrUpdateButtonText(isItemNew: Boolean): String {
    return if (isItemNew) { "Erinnerung hinzufügen" } else "Erinnerung speichern"
}

fun getCategoryRepresentation(category: ReminderCategory): String {
    return when(category) {
        ReminderCategory.GENERAL -> "Allgemein"
        ReminderCategory.BIRTHDAY -> "Geburtstag"
        ReminderCategory.AUTOMATIC_RENEW -> "Automatische Erneuerung"
        ReminderCategory.MANUAL_RENEW -> "Maneuelle Erneuerung"
        ReminderCategory.IMPORTANT_APPOINTMENT -> "Wichtiger Termin"
    }
}

fun getNotificationText(notification: ReminderNotification): String {
    val value = notification.valueBeforeDue

    if (value == 0)
        return "Am selben Tag"

    val interval = when(notification.interval) {
        ReminderNotificationInterval.DAYS -> if(value == 1) { "Tag" } else "Tage"
        ReminderNotificationInterval.MONTHS -> if(value == 1) { "Monat" } else "Monate"
    }

    return "$value $interval vorher"
}

fun getNotificationIntervalRepresentation(interval: ReminderNotificationInterval): String {
    return when(interval) {
        ReminderNotificationInterval.DAYS -> "Tage"
        ReminderNotificationInterval.MONTHS -> "Monate"
    }
}

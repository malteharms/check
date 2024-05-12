package de.malteharms.check.pages.reminder.presentation

import de.malteharms.check.data.TimePeriod
import de.malteharms.check.pages.reminder.data.ReminderSortType
import de.malteharms.check.pages.reminder.data.database.ReminderCategory
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
import de.malteharms.check.pages.reminder.data.database.ReminderNotificationInterval
import de.malteharms.check.pages.reminder.data.timeBetween
import java.time.LocalDateTime


fun getTextForDurationInDays(due: LocalDateTime): String {
    val periodBetweenTodayAndDue: TimePeriod = timeBetween(dateToReach = due)

    val prefix: String
    val dayText: String

    // handle values for years
    if (periodBetweenTodayAndDue.years > 0 || periodBetweenTodayAndDue.years < 0) {
        dayText = "${periodBetweenTodayAndDue.years} Jahren"
        prefix = if (periodBetweenTodayAndDue.years > 0) {
            "in"
        } else "vor"
    }

    else if (periodBetweenTodayAndDue.months > 0 || periodBetweenTodayAndDue.months < 0) {
        dayText = "${periodBetweenTodayAndDue.months} Monaten"
        prefix = if (periodBetweenTodayAndDue.months > 0) {
            "in"
        } else "vor"
    }

    else {
        dayText = when (periodBetweenTodayAndDue.days) {
            -1L -> "Gestern"
            0L -> "Heute"
            1L -> "Morgen"
            else -> "${periodBetweenTodayAndDue.days} Tagen"
        }

        prefix = when (periodBetweenTodayAndDue.days) {
            in Int.MIN_VALUE..-2 -> "vor"
            in 2..Int.MAX_VALUE -> "in"
            else -> ""
        }
    }

    dayText.removePrefix("-")
    return "$prefix $dayText".trim()
}

fun getAddOrUpdateButtonText(isItemNew: Boolean): String {
    return if (isItemNew) { "Erinnerung hinzufügen" } else "Erinnerung speichern"
}

fun getSortTypeRepresentation(sortType: ReminderSortType): String {
    return when(sortType) {
        ReminderSortType.TITLE -> "Titel"
        ReminderSortType.DUE_DATE -> "Fällig am"
    }
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

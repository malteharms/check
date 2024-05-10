package de.malteharms.check.pages.reminder.presentation

import de.malteharms.check.pages.reminder.data.ReminderSortType
import de.malteharms.check.pages.reminder.data.database.ReminderCategory
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
import de.malteharms.check.pages.reminder.data.database.ReminderNotificationInterval
import de.malteharms.check.pages.reminder.domain.convertTimestampToLocalDate
import de.malteharms.check.pages.reminder.domain.daysBetween
import java.time.LocalDate
import java.time.Period


fun getTextForDurationInDays(dueTimestamp: Long): String {
    val localDateOfDueTimestamp: LocalDate = convertTimestampToLocalDate(dueTimestamp)
    val periodBetweenTodayAndDue: Period = daysBetween(dateToReach = localDateOfDueTimestamp)

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
            -1 -> "Gestern"
            0 -> "Heute"
            1 -> "Morgen"
            else -> "${periodBetweenTodayAndDue.days} Tagen"
        }

        prefix = when (periodBetweenTodayAndDue.days) {
            in Int.MIN_VALUE..-2 -> "vor"
            in Int.MAX_VALUE..Int.MAX_VALUE -> "in"
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

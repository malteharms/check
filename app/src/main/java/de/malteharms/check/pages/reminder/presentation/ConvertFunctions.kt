package de.malteharms.check.pages.reminder.presentation

import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.NotificationItem
import de.malteharms.utils.logic.timeBetween
import de.malteharms.utils.model.DateExt
import de.malteharms.utils.model.TimePeriod
import java.time.temporal.ChronoUnit


fun getTextForDurationInDays(
    due: DateExt,
    today: DateExt = DateExt.now()
): String {
    val periodBetweenTodayAndDue: TimePeriod = timeBetween(end = due, start = today)

    val dayText: String

    // handle values for years
    if (periodBetweenTodayAndDue.years > 0 || periodBetweenTodayAndDue.years < 0) {
        dayText = "${periodBetweenTodayAndDue.years} J"
    }

    else if (periodBetweenTodayAndDue.months > 0 || periodBetweenTodayAndDue.months < 0) {
        dayText = "${periodBetweenTodayAndDue.months} M"
    }

    else {
        dayText = when (periodBetweenTodayAndDue.days) {
            0L -> "Heute"
            -1L -> "Gestern"
            1L-> "Morgen"
            else -> "${periodBetweenTodayAndDue.days} T"
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

    val timeUnitText: String = when (notification.timeUnit) {
        ChronoUnit.DAYS -> if (notification.valueBeforeDue == 1L) "Tag" else "Tage"
        ChronoUnit.MONTHS -> if (notification.valueBeforeDue == 1L) "Monat" else "Monate"
        else -> throw UnsupportedOperationException("Wrong time unit parsed")
    }
    return "${notification.valueBeforeDue} $timeUnitText vorher"
}

fun getNotificationIntervalRepresentation(timeUnit: ChronoUnit): String {
    return when(timeUnit) {
        ChronoUnit.DAYS -> "Tage"
        ChronoUnit.MONTHS -> "Monate"
        else -> throw UnsupportedOperationException("Wrong time unit parsed")
    }
}

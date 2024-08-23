package de.malteharms.check.pages.reminder.presentation

import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.NotificationItem
import java.time.temporal.ChronoUnit

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

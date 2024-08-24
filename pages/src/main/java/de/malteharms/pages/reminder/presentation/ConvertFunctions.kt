package de.malteharms.pages.reminder.presentation

import de.malteharms.database.tables.ReminderCategory
import de.malteharms.database.tables.NotificationItem
import java.time.temporal.ChronoUnit

fun getCategoryRepresentation(category: de.malteharms.database.tables.ReminderCategory): String {
    return when(category) {
        de.malteharms.database.tables.ReminderCategory.GENERAL -> "Allgemein"
        de.malteharms.database.tables.ReminderCategory.BIRTHDAY -> "Geburtstag"
        de.malteharms.database.tables.ReminderCategory.IMPORTANT_APPOINTMENT -> "Wichtiger Termin"
    }
}

fun getNotificationText(
    notification: de.malteharms.database.tables.NotificationItem
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

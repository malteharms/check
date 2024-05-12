package de.malteharms.check.pages.reminder.domain

import de.malteharms.check.data.TimePeriod
import de.malteharms.check.pages.reminder.data.database.ReminderNotificationInterval
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone


/* Utility */
fun getCurrentTimestamp(): Long {
    val currentDate = LocalDate.now()
    return convertLocalDateToTimestamp(currentDate)
}

fun getDeviceTimeZone(): ZoneId {
    return TimeZone.getDefault().toZoneId()
}

fun timeBetween(dateToReach: LocalDateTime, today: LocalDateTime = LocalDateTime.now()): TimePeriod {
    return TimePeriod(
        days = today.until(dateToReach, ChronoUnit.DAYS),
        months = today.until(dateToReach, ChronoUnit.MONTHS),
        years = today.until(dateToReach, ChronoUnit.YEARS),
    )
}

// TODO Test this method
fun calculateNotificationDate(
    dueDate: LocalDateTime,
    valueForNotification: Int,
    daysOrMonths: ReminderNotificationInterval
): LocalDateTime {
    return when (daysOrMonths) {
        ReminderNotificationInterval.DAYS -> dueDate.minusDays(valueForNotification.toLong())
        ReminderNotificationInterval.MONTHS -> dueDate.minusMonths(valueForNotification.toLong())
    }
}

/* Timestamp <-> LocalDate */
fun convertTimestampToLocalDate(timestamp: Long): LocalDate {
    return Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
}

fun convertLocalDateToTimestamp(date: LocalDate): Long {
    val zoneId = getDeviceTimeZone()
    val localDateTime = date.atStartOfDay(zoneId)

    return localDateTime.toEpochSecond()
}

/* Timestamp <-> String */
fun convertTimestampToDateString(date: LocalDateTime): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
    return formatter.format(date)
}

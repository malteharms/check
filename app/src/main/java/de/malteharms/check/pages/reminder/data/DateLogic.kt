package de.malteharms.check.pages.reminder.data

import de.malteharms.check.data.TimePeriod
import de.malteharms.check.pages.reminder.data.database.ReminderNotificationInterval
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone


/* Utility */
fun timeBetween(dateToReach: LocalDateTime, today: LocalDateTime = LocalDate.now().atStartOfDay()): TimePeriod {
    return TimePeriod(
        days = today.until(dateToReach, ChronoUnit.DAYS),
        months = today.until(dateToReach, ChronoUnit.MONTHS),
        years = today.until(dateToReach, ChronoUnit.YEARS),
    )
}

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

/* Timestamp <-> String */
fun convertTimestampToDateString(date: LocalDateTime): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return date.format(formatter)
}

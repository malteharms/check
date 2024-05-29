package de.malteharms.check.pages.reminder.data

import de.malteharms.check.data.TimePeriod
import de.malteharms.check.data.database.tables.ReminderNotificationInterval
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/* Utility */
fun timeBetween(end: LocalDateTime, start: LocalDateTime = LocalDate.now().atStartOfDay()): TimePeriod {
    return TimePeriod(
        days = start.until(end, ChronoUnit.DAYS),
        months = start.until(end, ChronoUnit.MONTHS),
        years = start.until(end, ChronoUnit.YEARS),
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

fun getValueBeforeDueAndInterval(
    notificationDate: LocalDateTime,
    dueDate: LocalDateTime
): Pair<Long, ReminderNotificationInterval> {

    val period: TimePeriod = timeBetween(
        end = dueDate,
        start = notificationDate
    )

    val interval: ReminderNotificationInterval = when {
        period.months > 0 -> ReminderNotificationInterval.MONTHS
        else -> ReminderNotificationInterval.DAYS
    }

    val value: Long = when {
        period.months > 0 -> period.months
        else -> period.days
    }

    return Pair(value, interval)
}

fun checkIfBirthdayNeedsToBeUpdated(
    dateToReview: LocalDateTime,
    today: LocalDateTime = LocalDate.now().atStartOfDay(),
    overDue: Long = 0
): Boolean {
    val thisYear: Int = today.year
    return dateToReview.minusDays(overDue).withYear(thisYear).isBefore(today)
}

fun calculateCorrectYearOfNextBirthday(
    dateToReview: LocalDateTime,
    today: LocalDateTime = LocalDate.now().atStartOfDay(),
    overDue: Long = 0
): Int {
    val birthdayNeedsUpdate: Boolean = checkIfBirthdayNeedsToBeUpdated(
        dateToReview,
        overDue = overDue
    )

    val nextYearOfBirthday: Int = if (birthdayNeedsUpdate) {
        today.year + 1
    } else today.year

    return nextYearOfBirthday
}

/* Timestamp <-> String */
fun convertTimestampToDateString(date: LocalDateTime): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return date.format(formatter)
}

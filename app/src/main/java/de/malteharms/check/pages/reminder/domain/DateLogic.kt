package de.malteharms.check.pages.reminder.domain

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date
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

fun daysBetween(dateToReach: LocalDate, today: LocalDate = LocalDate.now()): Period {
    return today.until(dateToReach)
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
fun convertTimestampToDateString(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
    val date = Date(timestamp * 1000)
    return formatter.format(date)
}

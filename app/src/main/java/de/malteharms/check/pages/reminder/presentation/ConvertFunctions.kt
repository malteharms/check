package de.malteharms.check.pages.reminder.presentation

import de.malteharms.check.pages.reminder.domain.daysBetween
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun getTextForDurationInDays(date: LocalDate): String {
    val amountDays: Long = daysBetween(date)

    val dayText: String = when (amountDays) {
        -1L -> "Gestern"
        0L -> "Heute"
        1L -> "Morgen"
        else -> "Tagen"
    }

    val prefix: String = when {
        (amountDays < -1L) -> "vor $amountDays "
        (amountDays > 1L) -> "in $amountDays "
        else -> ""
    }

    return "$prefix$dayText"
}


fun getGermanDateFormat(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return date.format(formatter)
}

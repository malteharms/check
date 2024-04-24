package de.malteharms.check.pages.reminder.domain

import java.time.LocalDate
import java.time.temporal.ChronoUnit


fun daysBetween(pastDate: LocalDate): Long {
    val today = LocalDate.now()
    return ChronoUnit.DAYS.between(pastDate, today)
}


package de.malteharms.check.pages.reminder.data

import java.time.LocalDate

enum class ReminderNotificationRange {
    DAYS,
    MONTHS
}

class ReminderNotification(date: String) {

    private val dateOfNotification: LocalDate

    init {
        val dateSplit: List<String> = date.split('.')

        dateOfNotification = LocalDate.of(
            Integer.valueOf(dateSplit[2]),
            Integer.valueOf(dateSplit[1]),
            Integer.valueOf(dateSplit[0])
        )
    }

    fun exportForDatabase(): String {
        val day = dateOfNotification.dayOfMonth
        val month = dateOfNotification.month
        val year = dateOfNotification.year

        return "$day.$month.$year"
    }



}
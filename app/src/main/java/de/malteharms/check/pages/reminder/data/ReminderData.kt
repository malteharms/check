package de.malteharms.check.pages.reminder.data

import java.time.LocalDate

data class ReminderData(
    val id: Int,
    val iconId: Int,
    val title: String,
    val description: String,
    val dueDate: LocalDate,
)

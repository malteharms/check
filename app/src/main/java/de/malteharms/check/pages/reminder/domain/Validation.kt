package de.malteharms.check.pages.reminder.domain

fun validateNewReminderEntry(title: String): Boolean {
    return title != ""
}

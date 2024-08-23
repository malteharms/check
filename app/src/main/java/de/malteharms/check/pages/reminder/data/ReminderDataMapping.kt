package de.malteharms.check.pages.reminder.data

import de.malteharms.check.R
import de.malteharms.database.tables.ReminderCategory

fun getIconIdByReminderCategory(category: de.malteharms.database.tables.ReminderCategory): Int {
    return when(category) {
        de.malteharms.database.tables.ReminderCategory.GENERAL -> R.drawable.reminder_general
        de.malteharms.database.tables.ReminderCategory.BIRTHDAY -> R.drawable.reminder_birthday
        de.malteharms.database.tables.ReminderCategory.IMPORTANT_APPOINTMENT -> R.drawable.reminder_important_event
    }
}
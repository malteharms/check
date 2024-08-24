package de.malteharms.pages.reminder.data

import de.malteharms.database.tables.ReminderCategory
import de.malteharms.pages.R


fun getIconIdByReminderCategory(category: ReminderCategory): Int {
    return when(category) {
        ReminderCategory.GENERAL -> R.drawable.reminder_general
        ReminderCategory.BIRTHDAY -> R.drawable.reminder_birthday
        ReminderCategory.IMPORTANT_APPOINTMENT -> R.drawable.reminder_important_event
    }
}
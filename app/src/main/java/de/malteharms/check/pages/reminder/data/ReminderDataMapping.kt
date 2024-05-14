package de.malteharms.check.pages.reminder.data

import de.malteharms.check.R
import de.malteharms.check.data.database.tables.ReminderCategory

fun getIconIdByReminderCategory(category: ReminderCategory): Int {
    return when(category) {
        ReminderCategory.GENERAL -> R.drawable.reminder_general
        ReminderCategory.BIRTHDAY -> R.drawable.reminder_birthday
        ReminderCategory.AUTOMATIC_RENEW -> R.drawable.reminder_automatic_renew
        ReminderCategory.MANUAL_RENEW -> R.drawable.reminder_manual_renew
        ReminderCategory.IMPORTANT_APPOINTMENT -> R.drawable.reminder_important_event
    }
}
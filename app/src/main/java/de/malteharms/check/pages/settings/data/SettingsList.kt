package de.malteharms.check.pages.settings.data

import de.malteharms.check.data.database.tables.Setting

fun getAllSettings(): List<List<Setting>> {
    return listOf(
        getReminderSettings()
    )
}

fun getReminderSettings(): List<Setting> {
    return listOf(
        ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS.getSetting(),
        ReminderSettings.DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS.getSetting()
    )
}

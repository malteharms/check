package de.malteharms.check.pages.settings.data

data class SettingsState(

    val syncBirthdayThroughContacts: Boolean =
        ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS.defaultValue().boolean!!,

    val defaultNotificationForBirthday: Boolean =
        ReminderSettings.DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS.defaultValue().boolean!!

)

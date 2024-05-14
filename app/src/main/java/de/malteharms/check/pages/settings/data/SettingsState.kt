package de.malteharms.check.pages.settings.data

data class SettingsState(

    val syncBirthdayThroughContacts: Boolean = ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS.defaultValue().boolean!!

)

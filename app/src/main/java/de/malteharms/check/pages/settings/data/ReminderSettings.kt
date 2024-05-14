package de.malteharms.check.pages.settings.data

import de.malteharms.check.pages.settings.domain.AnySetting

enum class ReminderSettings: AnySetting {

    SYNC_BIRTHDAYS_THROUGH_CONTACTS;

    override fun getTitle(): String {
        return when(this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> "Geburtstage aus Kontakten"
        }
    }

    override fun defaultValue(): SettingValue {
        return when (this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> SettingValue(boolean = false)
        }
    }

    override fun getDescription(): String {
        return when (this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> "Synchronisiere die Geburtstage aus deinen Kontakten"
        }
    }

    override fun getGroup(): SettingsGroup {
        return when (this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> SettingsGroup.REMINDER
        }
    }

}
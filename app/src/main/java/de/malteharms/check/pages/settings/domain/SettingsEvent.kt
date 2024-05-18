package de.malteharms.check.pages.settings.domain

import de.malteharms.check.data.database.tables.Setting

sealed interface SettingsEvent {

    data class SwitchBirthdaySync(val setting: Setting, val value: Boolean) : SettingsEvent
    data class SwitchDefaultNotificationForBirthday(val setting: Setting, val value: Boolean): SettingsEvent

}

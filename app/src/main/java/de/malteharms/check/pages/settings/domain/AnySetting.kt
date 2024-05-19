package de.malteharms.check.pages.settings.domain

import androidx.compose.runtime.Composable
import de.malteharms.check.data.database.tables.Setting
import de.malteharms.check.pages.settings.data.SettingValue
import de.malteharms.check.pages.settings.data.SettingsGroup
import de.malteharms.check.pages.settings.data.SettingsState

interface AnySetting {

    fun getTitle(): String
    fun defaultValue(): SettingValue
    fun getGroup(): SettingsGroup
    fun getDescription(): String

    @Composable
    fun Action(state: SettingsState, onEvent: (SettingsEvent) -> Unit)

    fun getSetting(): Setting {
        return Setting(
            item = this,
            value = this.defaultValue()
        )
    }
}

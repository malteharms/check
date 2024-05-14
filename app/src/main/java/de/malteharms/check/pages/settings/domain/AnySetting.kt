package de.malteharms.check.pages.settings.domain

import de.malteharms.check.data.database.tables.Setting
import de.malteharms.check.pages.settings.data.SettingValue
import de.malteharms.check.pages.settings.data.SettingsGroup

interface AnySetting {

    fun getTitle(): String
    fun defaultValue(): SettingValue
    fun getGroup(): SettingsGroup
    fun getDescription(): String

    fun getSetting(): Setting {
        return Setting(
            item = this,
            value = this.defaultValue()
        )
    }
}

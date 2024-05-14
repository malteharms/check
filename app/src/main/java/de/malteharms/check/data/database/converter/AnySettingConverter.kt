package de.malteharms.check.data.database.converter

import androidx.room.TypeConverter
import de.malteharms.check.pages.settings.data.ReminderSettings
import de.malteharms.check.pages.settings.data.SettingValue
import de.malteharms.check.pages.settings.data.SettingsGroup
import de.malteharms.check.pages.settings.domain.AnySetting

class AnySettingConverter {

    @TypeConverter
    fun toAnySetting(value: String): AnySetting {
        val split = value.split(':')

        val group: SettingsGroup = SettingsGroup.valueOf(split[0])

        return when (group) {
            SettingsGroup.GENERAL -> TODO()
            SettingsGroup.CASH -> TODO()
            SettingsGroup.FOOD -> TODO()
            SettingsGroup.HOME -> TODO()
            SettingsGroup.PROFILE -> TODO()
            SettingsGroup.REMINDER -> ReminderSettings.valueOf(split[1])
            SettingsGroup.TODO -> TODO()
        }

    }

    @TypeConverter
    fun fromSettingValue(setting: AnySetting): String {
        return "${setting.getGroup()}:${setting}"
    }

}
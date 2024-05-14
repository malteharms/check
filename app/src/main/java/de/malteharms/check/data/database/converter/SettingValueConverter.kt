package de.malteharms.check.data.database.converter

import androidx.room.TypeConverter
import de.malteharms.check.pages.settings.data.SettingValue

class SettingValueConverter {
    @TypeConverter
    fun toAnySetting(dbValue: String): SettingValue {
        val split: List<String> = dbValue.split(':')
        val value: String = split[1]

        return when (split[0]) {
            "boolean" -> SettingValue(boolean = value == "true")
            else -> SettingValue(string = value)
        }

    }

    @TypeConverter
    fun fromSettingValue(setting: SettingValue): String {
        return if (setting.boolean != null) {
            "boolean:${setting.boolean}"
        } else {
            "string:${setting.string}"
        }
    }
}
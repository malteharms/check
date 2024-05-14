package de.malteharms.check.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.malteharms.check.pages.settings.data.SettingValue
import de.malteharms.check.pages.settings.data.SettingsGroup
import de.malteharms.check.pages.settings.domain.AnySetting

@Entity(tableName = "settings")
data class Setting (
    @PrimaryKey
    val item: AnySetting,
    val value: SettingValue
)

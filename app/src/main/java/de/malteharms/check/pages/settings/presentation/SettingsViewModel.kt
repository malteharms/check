package de.malteharms.check.pages.settings.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.data.database.tables.Setting
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.pages.settings.data.ReminderSettings
import de.malteharms.check.pages.settings.data.SettingValue
import de.malteharms.check.pages.settings.data.SettingsState
import de.malteharms.check.pages.settings.data.getAllSettings
import de.malteharms.check.pages.settings.domain.SettingsEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dao: CheckDao,
    private val syncContacts: () -> Unit
): ViewModel() {

    companion object {
        private val TAG: String? = SettingsViewModel::class.simpleName
    }

    init {
        getAllSettings().forEach { settingsGroup ->
            settingsGroup.forEach { setting ->
                // make sure, that all settings are contained in the database
                viewModelScope.launch {
                    dao.insertSetting(setting)
                }
            }
        }
        Log.i(TAG, "Settings are created in database")
    }

    val state = MutableStateFlow(SettingsState(
        syncBirthdayThroughContacts = dao.getSettingsValue(ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS).boolean!!
    ))

    fun onEvent(event: SettingsEvent) {

        when (event) {
            is SettingsEvent.SwitchBirthdaySync -> {
                state.update { it.copy(
                    syncBirthdayThroughContacts = event.value
                ) }

                val updatedValue: Setting = event.setting.copy(
                    value = SettingValue(boolean = event.value)
                )

                viewModelScope.launch {
                    dao.updateSetting(updatedValue)
                }

                Log.i(TAG, "Updated setting ${event.setting.item.getTitle()} to ${event.value}")
                syncContacts()
            }
        }
    }

}
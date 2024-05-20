package de.malteharms.check.pages.settings.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.check.CheckApp
import de.malteharms.check.data.NotificationResult
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.data.database.tables.ReminderNotificationInterval
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.data.database.tables.Setting
import de.malteharms.check.data.notification.dataclasses.AlarmItem
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.pages.reminder.presentation.getNotificationIntervalRepresentation
import de.malteharms.check.pages.settings.data.ReminderSettings
import de.malteharms.check.pages.settings.data.SettingValue
import de.malteharms.check.pages.settings.data.SettingsState
import de.malteharms.check.pages.settings.data.getAllSettings
import de.malteharms.check.pages.settings.domain.SettingsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.forEach
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

    val notificationScheduler = CheckApp.appModule.notificationScheduler

    val state = MutableStateFlow(SettingsState())

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

    fun onEvent(event: SettingsEvent) {

        when (event) {
            is SettingsEvent.SwitchBirthdaySync -> {
                state.update { it.copy(
                    syncBirthdayThroughContacts = event.value
                ) }

                val setting: Setting = ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS.getSetting()
                val updatedValue: Setting = setting.copy(
                    value = SettingValue(boolean = event.value)
                )

                viewModelScope.launch {
                    dao.updateSetting(updatedValue)
                }

                Log.i(TAG, "Updated setting ${setting.item.getTitle()} to ${event.value}")
                syncContacts()
            }

            is SettingsEvent.SwitchDefaultNotificationForBirthday -> {
                state.update { it.copy(
                    defaultNotificationForBirthday = event.value
                ) }

                val setting: Setting = ReminderSettings.DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS.getSetting()

                val updatedValue: Setting = setting.copy(
                    value = SettingValue(boolean = event.value)
                )

                viewModelScope.launch {
                    dao.updateSetting(updatedValue)

                    val allBirthdays: Flow<List<ReminderItem>> = dao.getFilteredReminderItems(listOf(ReminderCategory.BIRTHDAY))
                    allBirthdays.collect { reminderItems ->
                        reminderItems.forEach{ reminderItem ->

                            val notifications: List<ReminderNotification> =
                                dao.getNotificationsForReminderItem(reminderItem.id)

                            when (state.value.defaultNotificationForBirthday) {
                                true -> {
                                    if (!notifications.any { it.valueBeforeDue == 0 }) {

                                        val result: NotificationResult = notificationScheduler.schedule(
                                            notificationId = null,
                                            item = AlarmItem(
                                                channel = NotificationChannel.REMINDER,
                                                time = reminderItem.dueDate,
                                                title = "Reminder >> ${reminderItem.title}",
                                                message = "Heute"
                                            )
                                        )

                                        dao.insertReminderNotification(ReminderNotification(
                                            reminderItem = reminderItem.id,
                                            valueBeforeDue = 0,
                                            interval = ReminderNotificationInterval.DAYS,
                                            notificationDate = reminderItem.dueDate,
                                            notificationId = result.notificationId
                                        ))
                                    }
                                }
                                false -> {
                                    notifications.forEach{ notification ->
                                        notificationScheduler.cancel(notification.notificationId)
                                        dao.removeReminderNotification(notification)
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    fun loadSettingsFromDatabase() {

        var syncBirthdayThroughContacts: SettingValue? = null
        var defaultNotificationForBirthday: SettingValue? = null

        viewModelScope.launch {
            syncBirthdayThroughContacts =
                dao.getSettingsValue(ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS)

            defaultNotificationForBirthday =
                dao.getSettingsValue(ReminderSettings.DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS)
        }

        if (syncBirthdayThroughContacts == null) {
            Log.w(TAG, "SyncBirthdayTroughContacts setting was not yet present in database. Choosing default value instead")
            syncBirthdayThroughContacts = ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS.defaultValue()
        }

        if (defaultNotificationForBirthday == null) {
            Log.w(TAG, "DefaultNotificationForBirthday setting was not yet present in database. Choosing default value instead")
            defaultNotificationForBirthday = ReminderSettings.DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS.defaultValue()
        }

        state.update { it.copy(
            syncBirthdayThroughContacts = syncBirthdayThroughContacts!!.boolean!!,
            defaultNotificationForBirthday = defaultNotificationForBirthday!!.boolean!!
        ) }
        Log.i(TAG, "Initialized settings and updated settings viewModel state")
    }

}
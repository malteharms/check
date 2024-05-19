package de.malteharms.check.pages.settings.data

import androidx.compose.runtime.Composable
import de.malteharms.check.pages.settings.domain.AnySetting
import de.malteharms.check.pages.settings.domain.SettingsEvent
import de.malteharms.check.pages.settings.presentation.settingAction.DefaultNotificationForBirthdaysAction
import de.malteharms.check.pages.settings.presentation.settingAction.SyncBirthdaysThoughContactsAction

enum class ReminderSettings: AnySetting {

    SYNC_BIRTHDAYS_THROUGH_CONTACTS,
    DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS;

    override fun getTitle(): String {
        return when(this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> "Geburtstage aus Kontakten"
            DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS -> "Benchrichtigung für Geburtstage"
        }
    }

    override fun defaultValue(): SettingValue {
        return when (this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> SettingValue(boolean = false)
            DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS -> SettingValue(boolean = false)
        }
    }

    override fun getDescription(): String {
        return when (this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> "Synchronisiere die Geburtstage aus deinen Kontakten"
            DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS -> "Du erhälst eine Benachrichtigung am Tag eines Geburstages"
        }
    }

    override fun getGroup(): SettingsGroup {
        return when (this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> SettingsGroup.REMINDER
            DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS -> SettingsGroup.REMINDER
        }
    }

    @Composable
    override fun Action(
        state: SettingsState,
        onEvent: (SettingsEvent) -> Unit
    ) {
        return when (this) {
            SYNC_BIRTHDAYS_THROUGH_CONTACTS -> SyncBirthdaysThoughContactsAction(
                state = state,
                onEvent = onEvent
            )
            DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS -> DefaultNotificationForBirthdaysAction(
                state = state,
                onEvent = onEvent
            )
        }
    }
}

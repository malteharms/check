package de.malteharms.check.pages.settings.presentation.settingAction

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.malteharms.check.pages.settings.data.ReminderSettings
import de.malteharms.check.pages.settings.data.SettingsState
import de.malteharms.check.pages.settings.domain.SettingsEvent

@Composable
fun DefaultNotificationForBirthdaysAction(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit
) {
    var defaultNotificationForBirthday: Boolean by remember {
        mutableStateOf(state.defaultNotificationForBirthday)
    }

    val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult =  { isGranted ->
            if (isGranted) {
                defaultNotificationForBirthday = !defaultNotificationForBirthday
                onEvent(SettingsEvent.SwitchDefaultNotificationForBirthday(
                    value = defaultNotificationForBirthday
                ))
            }
        }
    )

    Switch(
        checked = defaultNotificationForBirthday,
        onCheckedChange = {
            notificationPermissionResultLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    )
}
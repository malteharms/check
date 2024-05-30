package de.malteharms.check.pages.settings.presentation.settingAction

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                // if changing this logic, the code for API < 33 needs also to be adjusted
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
            // TODO #10
            //  no notification for API < 33
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionResultLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                defaultNotificationForBirthday = !defaultNotificationForBirthday
                onEvent(SettingsEvent.SwitchDefaultNotificationForBirthday(
                    value = defaultNotificationForBirthday
                ))
            }
        }
    )
}

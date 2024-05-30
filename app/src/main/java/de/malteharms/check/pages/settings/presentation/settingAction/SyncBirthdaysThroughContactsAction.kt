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
import de.malteharms.check.pages.settings.data.SettingsState
import de.malteharms.check.pages.settings.domain.SettingsEvent

@Composable
fun SyncBirthdaysThoughContactsAction(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit
) {

    var syncBirthdayThoughContacts: Boolean by remember {
        mutableStateOf(state.syncBirthdayThroughContacts)
    }

    val contactsPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult =  { isGranted ->

            if (isGranted) {
                syncBirthdayThoughContacts = true
                onEvent(SettingsEvent.SwitchBirthdaySync(value =  true))
            }
        }
    )

    Switch(
        checked = syncBirthdayThoughContacts,
        onCheckedChange = { isActive: Boolean ->

            if (isActive) {
                contactsPermissionResultLauncher.launch(Manifest.permission.READ_CONTACTS)
            } else {
                syncBirthdayThoughContacts = false
                onEvent(SettingsEvent.SwitchBirthdaySync(value = false))
            }

        }
    )
}
package de.malteharms.check.pages.settings.presentation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.pages.settings.data.ReminderSettings
import de.malteharms.check.pages.settings.data.SettingsState
import de.malteharms.check.pages.settings.data.getAllSettings
import de.malteharms.check.pages.settings.domain.SettingsEvent
import de.malteharms.check.ui.components.TopBar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SettingsPage(
    navController: NavController,
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit
) {

    var syncBirthdayThoughContacts: Boolean by remember {
        mutableStateOf(state.syncBirthdayThroughContacts)
    }

    var defaultNotificationForBirthday: Boolean by remember {
        mutableStateOf(state.defaultNotificationForBirthday)
    }

    var hasReadingContactsPermission: Boolean by remember {
        mutableStateOf(false)
    }

    val contactsPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult =  { isGranted ->
            hasReadingContactsPermission = isGranted
        }
    )

    Scaffold(
        topBar = { TopBar(navController = navController, title = "Settings") }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            
            getAllSettings().forEachIndexed { outerIndex, settings -> 
                
                LazyColumn {
                    itemsIndexed(settings) { index, setting ->

                        val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestPermission(),
                            onResult =  { isGranted ->
                                if (isGranted) {
                                    defaultNotificationForBirthday = !defaultNotificationForBirthday
                                    onEvent(SettingsEvent.SwitchBirthdaySync(setting, defaultNotificationForBirthday))
                                }
                            }
                        )

                        Column (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            
                            // first element fill also add a header
                            if (index == 0) {
                                if (outerIndex != 0) {
                                    HorizontalDivider()
                                }
                                Text(text = setting.item.getGroup().toString())
                            }

                            // actual setting
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                
                                Column {
                                    Text(setting.item.getTitle())
                                    Text(setting.item.getDescription(), fontSize = MaterialTheme.typography.bodySmall.fontSize)
                                }

                                when (setting.item) {
                                    ReminderSettings.SYNC_BIRTHDAYS_THROUGH_CONTACTS -> {
                                        Switch(
                                            checked = syncBirthdayThoughContacts,
                                            onCheckedChange = {
                                                contactsPermissionResultLauncher.launch(
                                                    Manifest.permission.READ_CONTACTS
                                                )

                                                if (hasReadingContactsPermission) {
                                                    syncBirthdayThoughContacts = it
                                                    onEvent(SettingsEvent.SwitchBirthdaySync(setting, it))
                                                }
                                            }
                                        )
                                    }

                                    ReminderSettings.DEFAULT_NOTIFICATION_DATE_FOR_BIRTHDAYS -> {
                                        Switch(
                                            checked = defaultNotificationForBirthday,
                                            onCheckedChange = {
                                                notificationPermissionResultLauncher.launch(
                                                    Manifest.permission.POST_NOTIFICATIONS
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
            }
        }
    }
}

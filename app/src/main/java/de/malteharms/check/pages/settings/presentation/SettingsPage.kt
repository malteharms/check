package de.malteharms.check.pages.settings.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import de.malteharms.check.ui.components.TopBar

@Composable
fun SettingsPage(
    navController: NavController
) {

    Scaffold(
        topBar = { TopBar(navController = navController, title = "Settings") }
    ) { paddingValues ->

        val mCheckedState = remember{
            mutableStateOf(false)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Synchronisieren der Geburtstage über Kontakte")
                    Switch(
                        checked = mCheckedState.value,
                        onCheckedChange = {
                            mCheckedState.value = it

                            if (mCheckedState.value) {
                                Log.i("Settings", "Synchronize with Google Calendar")
                            }
                        }
                    )
                }

            }
        }

    }

}

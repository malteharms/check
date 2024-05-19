package de.malteharms.check.pages.settings.presentation

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.pages.settings.data.SettingsState
import de.malteharms.check.pages.settings.data.getAllSettings
import de.malteharms.check.pages.settings.domain.SettingsEvent
import de.malteharms.check.presentation.components.TopBar

@Composable
fun SettingsPage(
    navController: NavController,
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit
) {

    Scaffold(
        topBar = { TopBar(navController = navController, title = "Settings") }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            
            getAllSettings().forEachIndexed { outerIndex, settings -> 
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(settings) { index, setting ->

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

                                // the action Composable at the end of the row
                                setting.item.Action(state, onEvent)
                            }
                        }
                    }
                }
                
            }
        }
    }
}

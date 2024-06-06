package de.malteharms.check.pages.reminder.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.data.Screens
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.getBottomNavigationItems
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.AddReminderItemButton
import de.malteharms.check.pages.reminder.presentation.components.ReminderBottomSheet
import de.malteharms.check.pages.reminder.presentation.components.ReminderFilterRow
import de.malteharms.check.pages.reminder.presentation.components.ReminderItemRow
import de.malteharms.check.presentation.components.FloatingBottomNavigation
import de.malteharms.check.presentation.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderPage(
    navController: NavController,
    state: ReminderState,
    onEvent: (ReminderEvent) -> Unit,
    hasNotifications: (Long) -> Boolean,
    syncContacts: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    var currentEditItem: ReminderItem? by remember {
        mutableStateOf(null)
    }

    syncContacts()

    Scaffold (
        topBar = { TopBar(navController, "Reminder") },
        bottomBar = {
            FloatingBottomNavigation( navController, getBottomNavigationItems(), "Reminder")
        },
        floatingActionButton = {
            AddReminderItemButton(
                onClick = { onEvent(ReminderEvent.ShowNewDialog) }
            )
        }
    ){ paddingValues ->
        LazyColumn (
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(state.items) { reminderItem ->

                ReminderItemRow(
                    item = reminderItem,
                    hasNotifications = hasNotifications(reminderItem.id),
                    onClick =  {
                        currentEditItem = reminderItem
                        onEvent(ReminderEvent.ShowEditDialog(reminderItem))
                    }
                )
            }
            
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { navController.navigate(Screens.ReminderDetailsRoute.route) }
                    ) {
                        Text(text = "zeig' mir alle Reminder", color = Color.Gray)
                    }
                }
            }
        }
    }

    if (state.isEditingItem) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),

            onDismissRequest = {
                onEvent(ReminderEvent.UpdateItem(currentEditItem!!))
                currentEditItem = null
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            ReminderBottomSheet(
                item = currentEditItem,
                state = state,
                onEvent = onEvent
            )
        }
    }

    if (state.isAddingItem) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),

            onDismissRequest = {
                onEvent(ReminderEvent.HideDialog)
                currentEditItem = null
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            ReminderBottomSheet(
                item = null,
                state = state,
                onEvent = onEvent
            )
        }
    }
}

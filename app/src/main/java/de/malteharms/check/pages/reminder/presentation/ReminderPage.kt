package de.malteharms.check.pages.reminder.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.pages.reminder.data.ReminderSortType
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.AddReminderItemButton
import de.malteharms.check.pages.reminder.presentation.components.ReminderBottomSheet
import de.malteharms.check.pages.reminder.presentation.components.ReminderItemRow
import de.malteharms.check.ui.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderPage(
    navController: NavController,
    state: ReminderState,
    getNotifications: (Long) -> List<ReminderNotification>,
    onEvent: (ReminderEvent) -> Unit
) {

    val sheetState = rememberModalBottomSheetState()

    var currentEditItem: ReminderItem? by remember {
        mutableStateOf(null)
    }

    Scaffold (
        topBar = { TopBar(navController, "Reminder") },
        floatingActionButton = {
            AddReminderItemButton(
                onClick = { onEvent(ReminderEvent.ShowNewDialog) }
            )
        }
    ){ paddingValues ->
        LazyColumn (
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ReminderSortType.entries.forEach { sortType ->
                        val isSelected: Boolean = sortType == state.sortType

                        FilterChip(
                            onClick = { onEvent(ReminderEvent.SortItems(sortType)) },
                            label = { Text(getSortTypeRepresentation(sortType)) },
                            selected = isSelected,
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Done icon",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            },
                        )
                    }
                }
            }

            items(state.items) { reminderItem ->

                ReminderItemRow(
                    item = reminderItem,
                    onClick =  {
                        currentEditItem = reminderItem
                        onEvent(ReminderEvent.ShowEditDialog(reminderItem))
                    }
                )
            }
        }
    }

    if (state.isEditingItem) {
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
                item = currentEditItem,
                notifications = getNotifications(currentEditItem!!.id),
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
                notifications = listOf(),
                onEvent = onEvent
            )
        }
    }
}

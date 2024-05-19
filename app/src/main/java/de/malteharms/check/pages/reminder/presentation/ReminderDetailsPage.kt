package de.malteharms.check.pages.reminder.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.data.NAVIGATION_BAR_HEIGHT
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.ReminderNotification
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.presentation.components.ReminderBottomSheet
import de.malteharms.check.pages.reminder.presentation.components.ReminderItemRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDetailsPage(
    navController: NavController,
    state: ReminderState,
    onEvent: (ReminderEvent) -> Unit,
    getNotifications: (Long) -> List<ReminderNotification>
) {
    val sheetState = rememberModalBottomSheetState()

    var currentEditItem: ReminderItem? by remember {
        mutableStateOf(null)
    }

    LazyColumn {
        items(state.allItems) { item: ReminderItem ->
            ReminderItemRow(
                item = item,
                onClick =  {
                    currentEditItem = item
                    onEvent(ReminderEvent.ShowEditDialog(item))
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(NAVIGATION_BAR_HEIGHT.dp))
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
}
package de.malteharms.check.pages.reminder.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.malteharms.check.R
import de.malteharms.check.pages.reminder.data.ReminderData
import de.malteharms.check.pages.reminder.presentation.components.AddReminderItemButton
import de.malteharms.check.pages.reminder.presentation.components.ReminderBottomSheet
import de.malteharms.check.pages.reminder.presentation.components.ReminderItem
import de.malteharms.check.ui.theme.blue60
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderPage(navController: NavController) {

    val sampleItem = ReminderData(
        id = 1,
        iconId = R.drawable.ic_check,
        title = "TÜV",
        description = "Auto zum TÜV",
        dueDate = LocalDate.now(),
    )

    val sampleListOfReminderItems: List<ReminderData> = listOf(sampleItem)

    val reminderItemModifier: Modifier = Modifier
        .clip(shape = CircleShape)
        .fillMaxWidth()
        .background(blue60)
        .padding(5.dp)
        .padding(horizontal = 10.dp)

    val sheetState = rememberModalBottomSheetState()

    var showEditBottomSheet by remember { mutableStateOf(false) }
    var openedItem: ReminderData? by remember { mutableStateOf(null) }

    var showAddBottomSheet by remember { mutableStateOf(false) }

    Scaffold (
        floatingActionButton = {
            AddReminderItemButton(
                onClick = { showAddBottomSheet = true }
            )
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            sampleListOfReminderItems.forEach{item ->

                ReminderItem(
                    modifier = reminderItemModifier.clickable {
                        openedItem = item
                        showEditBottomSheet = true
                    },
                    icon = item.iconId,
                    title = item.title,
                    due = item.dueDate
                )

            }

        }
    }

    if (showEditBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showEditBottomSheet = false
                openedItem = null
            },
            sheetState = sheetState
        ) {
            ReminderBottomSheet(item = openedItem!!)
        }
    }

    if (showAddBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddBottomSheet = false
            },
            sheetState = sheetState
        ) {
            ReminderBottomSheet(item = null)
        }
    }
}

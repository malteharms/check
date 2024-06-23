package de.malteharms.check.pages.todo.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import de.malteharms.check.data.database.tables.TodoItem
import de.malteharms.check.data.getBottomNavigationItems
import de.malteharms.check.pages.reminder.presentation.components.bottomsheet.AddButton
import de.malteharms.check.pages.todo.data.TodoState
import de.malteharms.check.pages.todo.domain.TodoEvent
import de.malteharms.check.presentation.components.FloatingBottomNavigation
import de.malteharms.check.presentation.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Todo(
    navController: NavController,
    state: TodoState,
    onEvent: (TodoEvent) -> Unit
) {

    val sheetState = rememberModalBottomSheetState()

    var currentEditItem: TodoItem? by remember {
        mutableStateOf(null)
    }

    Scaffold(
        topBar = { TopBar(navController, title = "ToDo") },
        bottomBar = {
            FloatingBottomNavigation( navController, getBottomNavigationItems(), "ToDo")
        },
        floatingActionButton = {
            AddButton(
                text = "Todo hinzufügen",
                onClick = { onEvent(TodoEvent.ShowNewDialog) }
            )
        }
    ) { paddingValues ->
        LazyColumn (
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(state.items) { todoItem ->
                // todo ItemRow
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { /* navController.navigate(Screens.ReminderDetailsRoute.route) */ }
                    ) {
                        Text(text = "zeig' mir alle Todos", color = Color.Gray)
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
                onEvent(TodoEvent.UpdateItem(currentEditItem!!))
                currentEditItem = null
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            // todo TodoBottomSheet
        }
    }

    if (state.isAddingItem) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),

            onDismissRequest = {
                onEvent(TodoEvent.HideDialog)
                currentEditItem = null
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            // todo TodoBottomSheet
        }
    }
}
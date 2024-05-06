package de.malteharms.check.pages.reminder.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.check.data.database.CheckDao
import de.malteharms.check.pages.reminder.data.ReminderItem
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.pages.reminder.data.ReminderSortType
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.domain.getCurrentTimestamp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ReminderViewModel(
    private val dao: CheckDao
): ViewModel() {

    private val _reminderSortType = MutableStateFlow(ReminderSortType.DUE_DATE)
    private val _reminderItems = _reminderSortType
        .flatMapLatest { sortType ->
            when(sortType) {
                ReminderSortType.TITLE -> dao.getReminderItemsOrderedByTitle()
                ReminderSortType.DUE_DATE -> dao.getReminderItemsOrderedByDueDate()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _reminderState = MutableStateFlow(ReminderState())
    val state = combine(_reminderState, _reminderSortType, _reminderItems) { state, sortType, items ->
        state.copy(
            items = items,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReminderState())

    fun onEvent(event: ReminderEvent) {
        when (event) {

            ReminderEvent.ShowNewDialog -> {
                _reminderState.update { it.copy(
                    isAddingItem = true
                ) }
            }

            ReminderEvent.ShowEditDialog -> {
                _reminderState.update { it.copy(
                    isEditingItem = true
                ) }
            }

            ReminderEvent.HideDialog -> {
                _reminderState.update { it.copy(
                    isAddingItem = false,
                    isEditingItem = false
                ) }
            }

            ReminderEvent.SaveItem -> {
                val title = state.value.title.trim()
                val dueDate = state.value.dueDate

                if (title.isBlank() || dueDate == 0L) {
                    return
                }

                val currentTimestamp: Long = getCurrentTimestamp()

                val newReminderItem = ReminderItem(
                    title = title,
                    dueDate = dueDate,
                    lastUpdate = currentTimestamp,
                    creationDate = currentTimestamp
                )

                viewModelScope.launch {
                    dao.insertReminderItem(newReminderItem)
                }

                // reset to the default state
                _reminderState.update { it.copy(
                    isAddingItem = false,
                    title = "",
                    dueDate = getCurrentTimestamp()
                ) }

            }

            is ReminderEvent.UpdateItem -> {
                val title = state.value.title
                val dueDate = state.value.dueDate

                if (title.isBlank() || dueDate == 0L) {
                    return
                }

                val updatedReminderItem = ReminderItem(
                    id = event.itemToUpdate.id,
                    title = title,
                    dueDate = dueDate,
                    creationDate = event.itemToUpdate.creationDate,
                    lastUpdate = getCurrentTimestamp()
                )

                viewModelScope.launch {
                    dao.updateReminderItem(updatedReminderItem)
                }

                _reminderState.update { it.copy(
                    isAddingItem = false,
                    title = "",
                    dueDate = getCurrentTimestamp()
                ) }
            }

            is ReminderEvent.RemoveItem -> {
                viewModelScope.launch {
                    dao.removeReminderItem(reminderItem = event.item)
                }

                _reminderState.update { it.copy(
                    isAddingItem = false,
                    title = "",
                    dueDate = getCurrentTimestamp()
                ) }
            }


            is ReminderEvent.SetTitle -> {
                _reminderState.update { it.copy(
                    title = event.title
                ) }
            }

            is ReminderEvent.SetDueDate -> {
                _reminderState.update { it.copy(
                    dueDate = event.dueDate
                ) }
            }

            is ReminderEvent.SortItems -> {
                _reminderSortType.value = event.sortType
            }

        }
    }


}
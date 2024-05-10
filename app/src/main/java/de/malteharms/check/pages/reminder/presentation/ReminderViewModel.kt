package de.malteharms.check.pages.reminder.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.check.data.database.CheckDao
import de.malteharms.check.pages.reminder.data.database.ReminderItem
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.pages.reminder.data.ReminderSortType
import de.malteharms.check.pages.reminder.data.database.ReminderCategory
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.domain.calculateNotificationDate
import de.malteharms.check.pages.reminder.domain.convertLocalDateToTimestamp
import de.malteharms.check.pages.reminder.domain.convertTimestampToLocalDate
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

            is ReminderEvent.ShowEditDialog -> {

                var notifications: List<ReminderNotification> = listOf()
                viewModelScope.launch {
                     notifications = dao.getNotificationsForReminderItem(event.item.id)
                }

                _reminderState.update { it.copy(
                    title = event.item.title,
                    dueDate = event.item.dueDate,
                    category = event.item.category,
                    notifications = notifications,
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
                val category = state.value.category

                val notifications = state.value.newNotifications

                if (title.isBlank() || dueDate == 0L) {
                    return
                }

                val currentTimestamp: Long = getCurrentTimestamp()

                val newReminderItem = ReminderItem(
                    title = title,
                    dueDate = dueDate,
                    category = category,
                    lastUpdate = currentTimestamp,
                    creationDate = currentTimestamp
                )

                viewModelScope.launch {
                    val newItemId = dao.insertReminderItem(newReminderItem)

                    notifications.forEach {reminderNotification ->
                        val notificationLocaldate = calculateNotificationDate(
                            dueDate = convertTimestampToLocalDate(dueDate),
                            valueForNotification = reminderNotification.valueBeforeDue,
                            daysOrMonths = reminderNotification.interval
                        )

                        dao.insertReminderNotification(
                            ReminderNotification(
                                reminderItem = newItemId,
                                valueBeforeDue = reminderNotification.valueBeforeDue,
                                interval = reminderNotification.interval,
                                notificationDate = convertLocalDateToTimestamp(notificationLocaldate)
                            )
                        )
                    }
                }

                // reset to the default state
                _reminderState.update { it.copy(
                    isAddingItem = false,
                    isEditingItem = false,
                    title = "",
                    notifications = listOf(),
                    newNotifications = listOf(),
                    notificationsToDelete = listOf(),
                    category = ReminderCategory.GENERAL,
                    dueDate = getCurrentTimestamp()
                ) }

            }

            is ReminderEvent.UpdateItem -> {
                val title = state.value.title
                val dueDate = state.value.dueDate
                val category = state.value.category

                val newNotifications = state.value.newNotifications
                val notificationsToRemove = state.value.notificationsToDelete

                if (title.isBlank() || dueDate == 0L) {
                    return
                }

                val updatedReminderItem = ReminderItem(
                    id = event.itemToUpdate.id,
                    title = title,
                    dueDate = dueDate,
                    category = category,
                    creationDate = event.itemToUpdate.creationDate,
                    lastUpdate = getCurrentTimestamp()
                )

                viewModelScope.launch {
                    dao.updateReminderItem(updatedReminderItem)

                    newNotifications.forEach {reminderNotification ->
                        dao.insertReminderNotification(
                            ReminderNotification(
                                reminderItem = event.itemToUpdate.id,
                                valueBeforeDue = reminderNotification.valueBeforeDue,
                                interval = reminderNotification.interval,
                                notificationDate = reminderNotification.notificationDate
                            )
                        )
                    }

                    notificationsToRemove.forEach { reminderNotification ->
                        dao.removeReminderNotification(reminderNotification)
                    }
                }

                _reminderState.update { it.copy(
                    isAddingItem = false,
                    isEditingItem = false,
                    title = "",
                    notifications = listOf(),
                    newNotifications = listOf(),
                    notificationsToDelete = listOf(),
                    category = ReminderCategory.GENERAL,
                    dueDate = getCurrentTimestamp()
                ) }
            }

            is ReminderEvent.RemoveItem -> {
                viewModelScope.launch {
                    dao.removeReminderItem(reminderItem = event.item)
                    dao.removeReminderNotificationsForReminderItem(event.item.id)
                }

                _reminderState.update { it.copy(
                    isAddingItem = false,
                    isEditingItem = false,
                    title = "",
                    notifications = listOf(),
                    newNotifications = listOf(),
                    notificationsToDelete = listOf(),
                    category = ReminderCategory.GENERAL,
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

            is ReminderEvent.SetCategory -> {
                _reminderState.update { it.copy(
                    category = event.category
                ) }
            }

            is ReminderEvent.SortItems -> {
                _reminderSortType.value = event.sortType
            }

            is ReminderEvent.AddNotification -> {
                _reminderState.update { it.copy(
                    notifications = it.notifications.plus(event.notification),
                    newNotifications = it.newNotifications.plus(event.notification)
                ) }
            }

            is ReminderEvent.RemoveNotification -> {
                _reminderState.update { it.copy(
                    notifications = it.notifications.minus(event.notification),
                    notificationsToDelete = it.notificationsToDelete.plus(event.notification)
                ) }
            }
        }
    }

    fun getNotifications(itemId: Long): List<ReminderNotification> {
        return dao.getNotificationsForReminderItem(itemId)
    }

}

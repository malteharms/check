package de.malteharms.check.pages.reminder.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.check.domain.CheckDao
import de.malteharms.check.data.NotificationResult
import de.malteharms.check.data.NotificationState
import de.malteharms.check.data.notification.dataclasses.AlarmItem
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import de.malteharms.check.domain.AlarmScheduler
import de.malteharms.check.pages.reminder.data.database.ReminderItem
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.pages.reminder.data.ReminderSortType
import de.malteharms.check.pages.reminder.data.database.ReminderCategory
import de.malteharms.check.pages.reminder.data.database.ReminderNotification
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.data.calculateNotificationDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@OptIn(ExperimentalCoroutinesApi::class)
class ReminderViewModel(
    private val dao: CheckDao,
    private val notificationScheduler: AlarmScheduler
): ViewModel() {

    companion object {
        private val TAG: String? = ReminderViewModel::class.simpleName
    }

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
                Log.i(TAG, "Open 'new reminder' sheet")
                _reminderState.update { it.copy(
                    isAddingItem = true
                ) }
            }

            is ReminderEvent.ShowEditDialog -> {
                Log.i(TAG, "Open 'edit reminder' sheet")

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
                Log.i(TAG, "Hide add / edit reminder sheet")

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

                if (title.isBlank()) {
                    Log.w(TAG, "Cannot save reminder item with due date $dueDate, because the title is empty!")
                    return
                }

                val now: LocalDateTime = LocalDateTime.now()

                val newReminderItem = ReminderItem(
                    title = title,
                    dueDate = dueDate,
                    category = category,
                    lastUpdate = now,
                    creationDate = now
                )

                viewModelScope.launch {
                    // add reminder item into database
                    val newItemId: Long = dao.insertReminderItem(newReminderItem)
                    Log.i(TAG, "Inserted reminder $title with id $newItemId")

                    // add notifications to database and schedule them
                    notifications.forEach {reminderNotification ->
                        handleNotification(
                            newItemId,
                            newReminderItem,
                            reminderNotification
                        )
                    }
                }

                resetState()
            }

            is ReminderEvent.UpdateItem -> {
                val title: String = state.value.title
                val dueDate: LocalDateTime = state.value.dueDate
                val category: ReminderCategory = state.value.category

                val newNotifications = state.value.newNotifications
                val notificationsToRemove = state.value.notificationsToDelete

                if (title.isBlank()) {
                    Log.w(TAG, "Cannot update reminder item with due date $dueDate, because the title is empty!")
                    return
                }

                val updatedReminderItem = ReminderItem(
                    id = event.itemToUpdate.id,
                    title = title,
                    dueDate = dueDate,
                    category = category,
                    creationDate = event.itemToUpdate.creationDate,
                    lastUpdate = LocalDateTime.now()
                )

                viewModelScope.launch {
                    // update reminder item in database
                    dao.updateReminderItem(updatedReminderItem)
                    Log.i(TAG, "Updated reminder $title with id ${event.itemToUpdate.id}")

                    // remove deleted notifications
                    notificationsToRemove.forEach { reminderNotification ->
                        // cancel already scheduled notification
                        notificationScheduler.cancel(reminderNotification.notificationId)
                        // remove reminder from database
                        dao.removeReminderNotification(reminderNotification)

                        Log.i(TAG, "Removed and canceled notification scheduled for ${reminderNotification.notificationDate}")
                    }

                    // add new notifications to database and schedule them
                    newNotifications.forEach {reminderNotification ->
                        handleNotification(
                            event.itemToUpdate.id,
                            updatedReminderItem,
                            reminderNotification
                        )
                    }
                }

                resetState()
            }

            is ReminderEvent.RemoveItem -> {
                viewModelScope.launch {
                    dao.removeReminderNotificationsForReminderItem(event.item.id)
                    dao.removeReminderItem(reminderItem = event.item)
                }

                Log.i(TAG, "Removed all data which is related to reminder item ID ${event.item.id}")
                resetState()
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
                Log.i(TAG, "Due date changed to ${event.dueDate}")
            }

            is ReminderEvent.SetCategory -> {
                _reminderState.update { it.copy(
                    category = event.category
                ) }
                Log.i(TAG, "Category changed to ${event.category}")
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

    private suspend fun handleNotification(
        id: Long,
        reminderReference: ReminderItem,
        reminderNotification: ReminderNotification
    ) {
        // calculate the date, where notification needs to be thrown
        val notificationDate: LocalDateTime = calculateNotificationDate(
            dueDate = reminderReference.dueDate,
            valueForNotification = reminderNotification.valueBeforeDue,
            daysOrMonths = reminderNotification.interval
        )

        Log.i(TAG, "Calculated $notificationDate as notification date")

        // schedule notification for each item
        val result: NotificationResult = notificationScheduler.schedule(
            AlarmItem(
                channel = NotificationChannel.REMINDER,
                time = notificationDate,
                title = "Reminder >> ${reminderReference.title}",
                message = "in ${reminderNotification.valueBeforeDue} ${getNotificationIntervalRepresentation(reminderNotification.interval)}"
            )
        )

        if (result.state != NotificationState.SUCCESS) {
            Log.w(TAG, "Because notification could not be scheduled, notification will not be saved")
            return
        }

        // add notification to database
        dao.insertReminderNotification(
            ReminderNotification(
                reminderItem = id,
                valueBeforeDue = reminderNotification.valueBeforeDue,
                interval = reminderNotification.interval,
                notificationId = result.notificationId,
                notificationDate = notificationDate
            )
        )

        Log.i(TAG, "Inserted notification at $notificationDate with nId ${result.notificationId}")
    }

    private fun resetState() {
        _reminderState.update { it.copy(
            title = "",
            dueDate = LocalDateTime.now(),
            category = ReminderCategory.GENERAL,
            notifications = listOf(),
            newNotifications = listOf(),
            notificationsToDelete = listOf(),
            isAddingItem = false,
            isEditingItem = false
        ) }
        Log.i(TAG, "Reset internal cache for current reminder item")
    }

}

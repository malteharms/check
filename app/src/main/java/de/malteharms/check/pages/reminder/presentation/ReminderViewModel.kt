package de.malteharms.check.pages.reminder.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.check.CheckApp
import de.malteharms.check.data.notification.dataclasses.NotificationChannel
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.NotificationItem
import de.malteharms.check.data.notification.NotificationHandler
import de.malteharms.check.di.AppModule
import de.malteharms.check.pages.reminder.data.calculateNotificationDate
import de.malteharms.check.pages.reminder.domain.ReminderEvent
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
    private val app: AppModule
): ViewModel() {

    companion object {
        private val TAG: String? = ReminderViewModel::class.simpleName
    }

    private val dao = app.db.itemDao()

    private val _reminderFilter: MutableStateFlow<List<ReminderCategory>> = MutableStateFlow(
        emptyList()
    )

    private val _allReminderItems = _reminderFilter
        .flatMapLatest { filterList ->
            when (filterList.isEmpty()) {
                true -> dao.getAllReminderItems()
                false -> dao.getFilteredReminderItems(filterList)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _reminderItems = _reminderFilter
        .flatMapLatest { filterList ->
            when (filterList.isEmpty()) {
                true -> dao.getAllReminderItemsLimited(limit = 5)
                false -> dao.getFilteredReminderItemsLimited(filterList, limit = 5)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _reminderState = MutableStateFlow(ReminderState())

    val state = combine(
        _reminderState,
        _reminderFilter,
        _reminderItems,
        _allReminderItems
    ) { state, filter, items, allItems ->
        state.copy(
            items = items,
            allItems = allItems,
            filter = filter
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

                var notifications: List<NotificationItem> = listOf()
                viewModelScope.launch {
                     notifications = dao.getNotificationsForConnectedItem(
                         channel = NotificationChannel.REMINDER,
                         itemId = event.item.id
                     )
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
                    notifications.forEach { reminderNotification ->
                        val notification: NotificationItem? = NotificationHandler.scheduleNotification(
                            alarmScheduler = CheckApp.appModule.notificationScheduler,
                            type = NotificationChannel.REMINDER,
                            connectedItem = newReminderItem,
                            notificationDate = reminderNotification.notificationDate
                        )

                        if (notification != null) {
                            dao.insertNotification(notification)
                        }
                    }
                }

                resetState()
            }

            is ReminderEvent.UpdateItem -> {
                val title: String = state.value.title
                val dueDate: LocalDateTime = state.value.dueDate
                val category: ReminderCategory = state.value.category

                val notifications = state.value.notifications
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
                    birthdayRelation = event.itemToUpdate.birthdayRelation,
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
                        app.notificationScheduler.cancel(reminderNotification.notificationId)
                        // remove reminder from database
                        dao.removeNotification(reminderNotification)

                        Log.i(TAG, "Removed and canceled notification scheduled for ${reminderNotification.notificationDate}")
                    }

                    // (add new) / (update existing) notifications to database and schedule them
                    notifications.forEach { reminderNotification ->
                        val notification: NotificationItem? = NotificationHandler.scheduleNotification(
                            alarmScheduler = CheckApp.appModule.notificationScheduler,
                            type = NotificationChannel.REMINDER,
                            connectedItem = updatedReminderItem,
                            notificationDate = reminderNotification.notificationDate,
                            notificationId = event.itemToUpdate.id
                        )

                        if (notification != null) {
                            dao.insertNotification(notification)
                        }
                    }
                }

                resetState()
            }

            is ReminderEvent.RemoveItem -> {
                viewModelScope.launch {
                    dao.removeNotificationsForConnectedItem(
                        channel = NotificationChannel.REMINDER,
                        connectedItemId = event.item.id
                    )
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

            is ReminderEvent.AddOrRemoveFilterCategory -> {
                _reminderFilter.value = if (_reminderFilter.value.contains(event.filter)) {
                    _reminderFilter.value.minus(event.filter)
                } else _reminderFilter.value.plus(event.filter)
            }

            is ReminderEvent.AddDummyNotification -> {
                val notificationDate: LocalDateTime = calculateNotificationDate(
                    dueDate = _reminderState.value.dueDate,
                    valueForNotification = event.value,
                    daysOrMonths = event.interval
                )

                val dummyReminderNotification = NotificationItem(
                    connectedItem = -1, // will be set on save
                    channel = NotificationChannel.REMINDER,
                    notificationId = -1, // will be set on save
                    notificationDate = notificationDate
                )

                _reminderState.update { it.copy(
                    notifications = it.notifications.plus(dummyReminderNotification),
                    newNotifications = it.newNotifications.plus(dummyReminderNotification)
                ) }
            }

            is ReminderEvent.RemoveNotification -> {
                _reminderState.update { it.copy(
                    notifications = it.notifications.minus(event.notification),
                    notificationsToDelete = it.notificationsToDelete.plus(event.notification)
                ) }
            }

            ReminderEvent.MoveFromOrToDetailsScreen -> {
                _reminderFilter.value = emptyList()
            }

        }
    }

    fun getNotifications(itemId: Long): List<NotificationItem> {
        return dao.getNotificationsForConnectedItem(NotificationChannel.REMINDER, itemId)
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

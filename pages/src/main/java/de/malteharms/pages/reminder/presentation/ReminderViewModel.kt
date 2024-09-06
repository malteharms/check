package de.malteharms.pages.reminder.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteharms.database.CheckDao
import de.malteharms.database.tables.NotificationChannel
import de.malteharms.database.tables.NotificationItem
import de.malteharms.database.tables.reminder.ReminderItem
import de.malteharms.notification.data.NotificationHandler.Companion.scheduleNotification
import de.malteharms.notification.domain.AlarmScheduler
import de.malteharms.pages.reminder.data.ReminderState
import de.malteharms.pages.reminder.domain.ReminderEvent
import de.malteharms.utils.EmptyTitleException
import de.malteharms.utils.model.DateExt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val dao: CheckDao,
    private val notificationScheduler: AlarmScheduler
): ViewModel() {

    companion object {
        private val TAG: String? = ReminderViewModel::class.simpleName
    }

    private val _reminderState = MutableStateFlow(ReminderState())
    private val _items = dao
        .getAllReminders(limit = null, today = DateExt.now().toTimestamp())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state = combine(_reminderState, _items) { state, items ->
        state.copy(items = items)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReminderState())


    fun onEvent(event: ReminderEvent) {
        when (event) {

            ReminderEvent.HideEditorDialog -> {
                _reminderState.update { ReminderState() }
                Log.i(TAG, "Hide reminder item dialog, cleared viewmodel state")
            }

            is ReminderEvent.OpenEditorDialog -> {
                Log.i(TAG, "Open editor dialog for item ${event.item}")

                _reminderState.update { it.copy(
                    isAddingOrEditingItem = true
                ) }

                // when user is editing an existing reminder, copy
                // modifiable values into the current state
                event.item?.let { eventItem ->
                    viewModelScope.launch {
                        _reminderState.update { it.copy(
                            title = eventItem.title,
                            due = eventItem.due,
                            duration = eventItem.duration,
                            category = eventItem.category,
                            isTodo = eventItem.isTodo,
                            notifications = dao.getNotificationsForConnectedItem(eventItem.id)
                        ) }
                    }
                }
                Log.i(TAG, "Refreshed viewmodel state")
            }

            is ReminderEvent.SaveItem -> {
                val id = saveItem(event.id)
                _reminderState.update { ReminderState() }

                Log.i(TAG, "Saved reminder item (ID: $id) and cleared state")
            }

            is ReminderEvent.RemoveItem -> {
                viewModelScope.launch {
                    dao.removeNotificationsForConnectedItem(event.id)
                    dao.removeReminderById(event.id)
                }

                Log.i(TAG, "Removed all data which is related to reminder item ID ${event.id}")
            }

            is ReminderEvent.SetTitle -> {
                _reminderState.update { it.copy(
                    title = event.title
                ) }
            }
            is ReminderEvent.SetCategory -> {
                _reminderState.update { it.copy( category = event.id ) }
            }

            is ReminderEvent.SetDue -> {
                _reminderState.update { it.copy( due = event.due ) }
            }

            is ReminderEvent.SetDuration -> {
                _reminderState.update { it.copy( duration = event.duration ) }
            }

            is ReminderEvent.SetIsTodo -> {
                _reminderState.update { it.copy( isTodo = event.isTodo ) }
            }

            is ReminderEvent.AddNotification -> TODO()
            is ReminderEvent.AddNotificationDate -> TODO()

            is ReminderEvent.RemoveNotification -> {
                _reminderState.update { it.copy(
                    notifications = it.notifications.minus(event.id)
                ) }
            }

        }
    }

    private fun saveItem(existingId: Long?): Long {
        // save all collected values
        val newNotifications = state.value.notifications
        var newItem = ReminderItem(
            id = 0,
            title = state.value.title.trim(),
            due = state.value.due,
            duration = state.value.duration,
            category = state.value.category,
            isTodo = false,
        )

        // on error, the state does not need to be reset. The UI would still
        // show the editor and just show an information for the user
        if (newItem.title.isBlank()) {
            Log.w(TAG, "Cannot save reminder item because the title is empty!")
            throw EmptyTitleException("")
        }

        // save the id and creation date if the reminder already exist
        existingId?.let { id ->
            val existingItem: ReminderItem? = dao.getReminder(id)
            existingItem?.let { item ->
                newItem = newItem.copy(
                    id = id,
                    created = item.created
                )
            }
        }

        var newItemId: Long = -1
        viewModelScope.launch {
            // add reminder item into database
            newItemId = dao.upsertReminder(newItem)

            // clear all notifications for that item before inserting new ones
            dao.removeNotificationsForConnectedItem(newItemId)

            // schedule notification. On success, add it to database
            newNotifications.forEach { reminderNotification ->
                val notification: NotificationItem? = scheduleNotification(
                    alarmScheduler = notificationScheduler,
                    type = NotificationChannel.REMINDER,
                    connectedItem = newItem,
                    notificationDate = reminderNotification.notificationDate
                )

                // TODO the user does not get a warning that some notifications
                //  could not been added to the database. Invalid notifications
                //  would not be stored, but the user does not know.
                notification?.let { dao.insertNotification(it) }
            }
        }

        return newItemId
    }
}

package de.malteharms.pages.todo.presentation

import androidx.lifecycle.ViewModel
import de.malteharms.check.pages.reminder.data.ReminderState
import de.malteharms.pages.todo.data.TodoState
import de.malteharms.pages.todo.domain.TodoEvent
import kotlinx.coroutines.flow.MutableStateFlow

class TodoViewModel: ViewModel() {

    val state: MutableStateFlow<TodoState> = MutableStateFlow(TodoState())

    fun onEvent(event: TodoEvent) {

    }
}
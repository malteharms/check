package de.malteharms.check.pages.todo.data

import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.TodoItem

data class TodoState(

    val items: List<TodoItem> = emptyList(),

    val isAddingItem: Boolean = false,
    val isEditingItem: Boolean = false
)

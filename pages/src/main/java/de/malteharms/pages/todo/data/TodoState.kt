package de.malteharms.pages.todo.data

import de.malteharms.database.tables.todo.TodoItem

data class TodoState(

    val items: List<TodoItem> = emptyList(),

    val isAddingItem: Boolean = false,
    val isEditingItem: Boolean = false
)

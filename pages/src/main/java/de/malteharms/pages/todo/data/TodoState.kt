package de.malteharms.pages.todo.data

import de.malteharms.database.tables.TodoItem

data class TodoState(

    val items: List<de.malteharms.database.tables.TodoItem> = emptyList(),

    val isAddingItem: Boolean = false,
    val isEditingItem: Boolean = false
)

package de.malteharms.pages.todo.domain

import de.malteharms.database.tables.TodoItem

sealed interface TodoEvent {

    data object SaveItem: TodoEvent
    data class UpdateItem(val itemToUpdate: de.malteharms.database.tables.TodoItem): TodoEvent
    data class RemoveItem(val item: de.malteharms.database.tables.TodoItem): TodoEvent

    data object ShowNewDialog: TodoEvent
    data class ShowEditDialog(val item: de.malteharms.database.tables.TodoItem): TodoEvent

    data object HideDialog: TodoEvent
    
}
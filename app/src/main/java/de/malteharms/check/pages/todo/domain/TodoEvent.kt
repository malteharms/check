package de.malteharms.check.pages.todo.domain

import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.data.database.tables.TodoItem

sealed interface TodoEvent {

    data object SaveItem: TodoEvent
    data class UpdateItem(val itemToUpdate: TodoItem): TodoEvent
    data class RemoveItem(val item: TodoItem): TodoEvent

    data object ShowNewDialog: TodoEvent
    data class ShowEditDialog(val item: TodoItem): TodoEvent

    data object HideDialog: TodoEvent
    
}
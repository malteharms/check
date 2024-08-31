package de.malteharms.pages.todo.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import de.malteharms.database.tables.todo.TodoItem
import de.malteharms.utils.model.Priority
import de.malteharms.pages.components.presentation.ItemRow

@Composable
fun TodoItemRow(
    item: TodoItem,
    hasNotifications: Boolean,
    onClick: () -> Unit
) {

    ItemRow(
        title = item.title,
        dueDate = item.dueDate,
        hasNotifications = hasNotifications,
        onClick = { onClick() },
        leadingComposable = {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.DarkGray,      // todo color set though priority
                    radius = size.minDimension / 4,
                    alpha = 1f,
                    style = Stroke(
                        width = 10f
                    )
                )
            }
        },
        trailingComposable = {

        }
    )

}

@Preview
@Composable
fun TodoItemRowPreview() {

    val item = TodoItem(
        title = "Waschmaschine ausräumen",
        group = 0L,
        priority = Priority.NORMAL
    )

    TodoItemRow(
        item = item,
        hasNotifications = true,
        onClick = {}
    )
}
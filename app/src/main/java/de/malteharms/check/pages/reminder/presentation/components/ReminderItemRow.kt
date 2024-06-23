package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.pages.reminder.data.getIconIdByReminderCategory
import de.malteharms.check.pages.reminder.presentation.getTextForDurationInDays
import de.malteharms.check.presentation.components.ItemRow
import java.time.LocalDateTime


@Composable
fun ReminderItemRow(
    onClick: () -> Unit,
    hasNotifications: Boolean,
    item: ReminderItem
) {

    ItemRow(
        title = item.title,
        dueDate = item.dueDate,
        hasNotifications = hasNotifications,
        onClick = { onClick() },
        leadingComposable = {
            Icon(
                painter = painterResource(id = getIconIdByReminderCategory(item.category)),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingComposable = {
            Text(
                textAlign = TextAlign.Center,
                text = getTextForDurationInDays(item.dueDate),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                maxLines = 2,
                minLines = 1
            )
        }
    )

}


@Preview
@Composable
fun ReminderItemRowPreview() {

    ReminderItemRow(
        {  },
        hasNotifications = true,
        ReminderItem(
            id = 0,
            title = "Personalausweis",
            dueDate = LocalDateTime.now().plusDays(1),
            category = ReminderCategory.GENERAL,
            creationDate = LocalDateTime.now(),
            lastUpdate = LocalDateTime.now(),
            todoRelation = null
        )
    )

}
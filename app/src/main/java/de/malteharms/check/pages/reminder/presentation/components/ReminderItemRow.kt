package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import de.malteharms.check.pages.reminder.data.getIconIdByReminderCategory
import de.malteharms.pages.components.presentation.ItemRow
import de.malteharms.utils.model.DateExt
import java.time.temporal.ChronoUnit


@Composable
fun ReminderItemRow(
    onClick: () -> Unit,
    hasNotifications: Boolean,
    item: de.malteharms.database.tables.ReminderItem
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
                text = item.dueDate.toStringUntilDue(),
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
        de.malteharms.database.tables.ReminderItem(
            id = 0,
            title = "Personalausweis",
            dueDate = DateExt.now().plus(value = 1, unit = ChronoUnit.DAYS),
            category = de.malteharms.database.tables.ReminderCategory.GENERAL,
            creationDate = DateExt.now(),
            lastUpdate = DateExt.now(),
            todoRelation = null
        )
    )

}
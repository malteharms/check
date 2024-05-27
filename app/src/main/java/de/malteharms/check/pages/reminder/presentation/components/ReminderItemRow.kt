package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.malteharms.check.R
import de.malteharms.check.data.database.tables.ReminderCategory
import de.malteharms.check.data.database.tables.ReminderItem
import de.malteharms.check.pages.reminder.data.convertTimestampToDateString
import de.malteharms.check.pages.reminder.data.getIconIdByReminderCategory
import de.malteharms.check.pages.reminder.presentation.getTextForDurationInDays
import java.time.LocalDateTime


@Composable
fun ReminderItemRow(
    onClick: () -> Unit,
    hasNotifications: Boolean,
    item: ReminderItem
) {

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(horizontal = 7.dp)
            .clickable { onClick() },
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = getIconIdByReminderCategory(item.category)),
                contentDescription = null,
                modifier = Modifier.weight(1f),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Column(
                modifier = Modifier.weight(7f)
            ) {
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(bottom = 2.dp))

                Row(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = convertTimestampToDateString(item.dueDate),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        fontWeight = FontWeight.Light
                    )

                    if (hasNotifications) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dott),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(4.dp)
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.Center,
                text = getTextForDurationInDays(item.dueDate),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                maxLines = 2,
                minLines = 1
            )
        }
    }


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
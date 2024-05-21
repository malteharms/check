package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import de.malteharms.check.presentation.theme.blue60
import de.malteharms.check.presentation.theme.blue70
import de.malteharms.check.presentation.theme.blue80
import java.time.LocalDateTime


@Composable
fun ReminderItemRow(
    onClick: () -> Unit,
    hasNotifications: Boolean,
    item: ReminderItem
) {

    Card(
        colors = CardDefaults.cardColors(containerColor = blue60),
        elevation = CardDefaults.cardElevation(defaultElevation = 50.dp),
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
                tint = Color.White
            )

            Column(
                modifier = Modifier.weight(7f)
            ) {
                Text(
                    text = item.title,
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
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        fontWeight = FontWeight.Light
                    )

                    if (hasNotifications) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dott),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(4.dp)
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.Center,
                text = getTextForDurationInDays(item.dueDate),
                fontWeight = FontWeight.Light,
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
            category = ReminderCategory.MANUAL_RENEW,
            creationDate = LocalDateTime.now(),
            lastUpdate = LocalDateTime.now(),
            todoRelation = null
        )
    )

}
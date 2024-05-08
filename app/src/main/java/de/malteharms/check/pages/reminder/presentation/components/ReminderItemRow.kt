package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.malteharms.check.pages.reminder.data.ReminderCategory
import de.malteharms.check.pages.reminder.data.ReminderItem
import de.malteharms.check.pages.reminder.data.getIconIdByReminderCategory
import de.malteharms.check.pages.reminder.presentation.getTextForDurationInDays
import de.malteharms.check.ui.theme.blue60


@Composable
fun ReminderItemRow(
    onClick: () -> Unit,
    item: ReminderItem
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(blue60)
            .padding(5.dp)
            .padding(horizontal = 10.dp)
            .clickable { onClick() }
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = getIconIdByReminderCategory(item.category)),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                ) // Image
                Text(text = item.title, color = Color.White)
            }


            Text(text = getTextForDurationInDays(item.dueDate), color = Color.White)
        }
    }

}


@Preview
@Composable
fun ReminderItemRowPreview() {

    ReminderItemRow(
        {  }, ReminderItem(
        id = 0,
        title = "Personalausweis",
        dueDate = 1715105986L,
        category = ReminderCategory.MANUAL_RENEW,
        creationDate = 1715105986L,
        lastUpdate = 1715105986L,
        todoRelation = null
    ))

}
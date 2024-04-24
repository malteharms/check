package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.malteharms.check.pages.reminder.presentation.getTextForDurationInDays
import java.time.LocalDate


@Composable
fun ReminderItem(
    modifier: Modifier,
    icon: Int,
    title: String,
    due: LocalDate
) {

    // todo 'due' needs to be formatted via presentation function

    Box(
        modifier = modifier
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = title)
            }

            Text(text = getTextForDurationInDays(due))
        }
    }

}

package de.malteharms.check.pages.reminder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.malteharms.check.pages.reminder.domain.convertTimestampToLocalDate
import de.malteharms.check.pages.reminder.presentation.getTextForDurationInDays
import java.time.LocalDate


@Composable
fun ReminderItem(
    modifier: Modifier,
    title: String,
    due: Long
) {
    Box(
        modifier = modifier
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = title)
            Text(text = getTextForDurationInDays(due))
        }
    }

}

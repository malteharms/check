package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.malteharms.check.pages.reminder.data.database.ReminderCategory
import de.malteharms.check.pages.reminder.data.database.ReminderItem
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.presentation.getCategoryRepresentation
@Composable
fun CategoryChoice(
    item: ReminderItem?,
    onEvent: (ReminderEvent) -> Unit
) {
    var selectedCategory by remember {
        mutableStateOf(item?.category ?: ReminderCategory.GENERAL)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ReminderCategory.entries.forEach { category ->

            FilterChip(
                onClick = {
                    selectedCategory = category
                    onEvent(ReminderEvent.SetCategory(category))
                },
                label = { Text(getCategoryRepresentation(category)) },
                selected = selectedCategory == category,
            )
        }
    }
}

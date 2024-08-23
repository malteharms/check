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
import de.malteharms.database.tables.ReminderCategory
import de.malteharms.database.tables.ReminderItem
import de.malteharms.check.pages.reminder.domain.ReminderEvent
import de.malteharms.check.pages.reminder.presentation.getCategoryRepresentation
@Composable
fun CategoryChoice(
    item: de.malteharms.database.tables.ReminderItem?,
    onEvent: (ReminderEvent) -> Unit,
    editable: Boolean
) {
    var selectedCategory by remember {
        mutableStateOf(item?.category ?: de.malteharms.database.tables.ReminderCategory.GENERAL)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        de.malteharms.database.tables.ReminderCategory.entries.forEach { category ->

            FilterChip(
                onClick = {
                    if (editable) {
                        selectedCategory = category
                        onEvent(ReminderEvent.SetCategory(category))
                    }
                },
                label = { Text(getCategoryRepresentation(category)) },
                selected = selectedCategory == category,
            )
        }
    }
}

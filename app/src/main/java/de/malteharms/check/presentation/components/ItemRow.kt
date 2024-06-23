package de.malteharms.check.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.malteharms.check.R
import de.malteharms.check.pages.reminder.data.convertTimestampToDateString
import java.time.LocalDateTime

@Composable
fun ItemRow(

    title: String,
    dueDate: LocalDateTime? = null,
    hasNotifications: Boolean = false,

    onClick: () -> Unit,

    leadingComposable: @Composable () -> Unit,
    trailingComposable: @Composable () -> Unit,

) {

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.padding(horizontal = 7.dp).height(60.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                leadingComposable()
            }

            Column(
                modifier = Modifier
                    .weight(7f)
                    .clickable { onClick() }
            ) {
                Box(
                    modifier = Modifier.weight(10f),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.weight(10f).fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dueDate != null) {
                        Text(
                            text = convertTimestampToDateString(dueDate),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            fontWeight = FontWeight.Light
                        )
                    }

                    if (hasNotifications) {
                        if (dueDate != null) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dott),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(4.dp)
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(1.5f), contentAlignment = Alignment.Center) {
                trailingComposable()
            }
        }
    }
}
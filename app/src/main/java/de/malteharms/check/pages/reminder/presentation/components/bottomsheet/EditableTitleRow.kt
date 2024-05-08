package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun EditableTitleRow(
    modifier: Modifier,
    alignment: Alignment.Vertical,
    arrangement: Arrangement.Horizontal,

    title: String,
    onValueChange: (String) -> Unit,
) {
    Row (
        modifier = modifier,
        horizontalArrangement = arrangement,
        verticalAlignment = alignment
    ){
        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        TextField(
            value = title,
            onValueChange = { newText ->  onValueChange(newText) },
            placeholder = { Text(text = "Titel", color = Color.LightGray) },
            modifier = Modifier.background(Color.Transparent),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
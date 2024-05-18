package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.malteharms.check.ui.components.CustomTextField

@Composable
fun EditableTitleRow(
    modifier: Modifier,
    alignment: Alignment.Vertical,
    arrangement: Arrangement.Horizontal,

    title: String,
    editable: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    Row (
        modifier = modifier,
        horizontalArrangement = arrangement,
        verticalAlignment = alignment
    ){
        Icon(imageVector = Icons.Default.Edit, contentDescription = null)

        if (editable) {
            CustomTextField(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(percent = 50)
                    )
                    .padding(horizontal = 10.dp),
                placeholderText = "Titel",
                value = title,
                onValueChange = { newText ->  onValueChange(newText) }
            )
        } else {
            Text(
                text = title,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(percent = 50)
                    )
                    .padding(horizontal = 10.dp),
                color = Color.Gray
            )
        }
    }
}


@Preview
@Composable
fun EditableTitleRowPreview() {
    EditableTitleRow(
        modifier = Modifier.fillMaxWidth(),
        alignment = Alignment.CenterVertically,
        arrangement = Arrangement.spacedBy(10.dp),
        title = "Perso",
        onValueChange = {}
    )
}

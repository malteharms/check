package de.malteharms.check.pages.reminder.presentation.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.malteharms.pages.components.presentation.CustomTextField

@Composable
fun EditableTitleRow(
    title: String,
    editable: Boolean = true,
    onValueChange: (String) -> Unit,
) {

    val modifier: Modifier = Modifier
        .background(
            Color.Transparent,
            MaterialTheme.shapes.large
        )
        .padding(horizontal = 10.dp)

    val textStyle = MaterialTheme.typography.titleLarge

    if (editable) {
        CustomTextField(
            modifier = modifier,
            placeholderText = "Titel",
            fontSize = textStyle.fontSize,
            value = title,
            onValueChange = { newText ->  onValueChange(newText) }
        )
    } else {
        Text(
            text = title,
            modifier = modifier,
            fontStyle = textStyle.fontStyle,
            color = Color.Gray
        )
    }

}


@Preview
@Composable
fun EditableTitleRowPreview() {
    EditableTitleRow(
        title = "Perso",
        onValueChange = {}
    )
}

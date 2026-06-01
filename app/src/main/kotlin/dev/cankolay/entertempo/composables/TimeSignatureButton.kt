package dev.cankolay.entertempo.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import dev.cankolay.entertempo.shared.model.TimeSignature

@Composable
fun TimeSignatureButton(
    timeSignature: TimeSignature,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
        ),
        shape = MaterialTheme.shapes.medium,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = timeSignature.toString())
    }
}

package dev.cankolay.entertempo.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable

@Composable
fun BpmValueButton(bpm: Int) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = {}
    ) {
        Text(text = "$bpm")
    }
}

package dev.cankolay.entertempo.wear.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton

@Composable
fun WearTempoAdjustButtons(
    enabled: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(size = 24.dp),
            enabled = enabled,
            onClick = onDecrease
        ) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus")
        }

        IconButton(
            modifier = Modifier.size(size = 24.dp),
            enabled = enabled,
            onClick = onIncrease
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Plus")
        }
    }
}

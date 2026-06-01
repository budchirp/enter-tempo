package dev.cankolay.entertempo.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.shared.model.MetronomeDefaults
import dev.cankolay.entertempo.shared.model.MetronomeState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TempoControls(
    state: MetronomeState,
    onCommand: (MetronomeCommand) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 32.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledIconButton(
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondary),
            enabled = !state.isPlaying,
            onClick = { onCommand(MetronomeCommand.AdjustBpm(delta = -MetronomeDefaults.bpmStep)) }
        ) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus")
        }

        FilledIconButton(
            modifier = Modifier.size(height = 96.dp, width = 160.dp),
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = MaterialTheme.shapes.extraExtraLarge,
            onClick = { onCommand(MetronomeCommand.TogglePlayback) }
        ) {
            Icon(
                imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (state.isPlaying) "Pause" else "Play"
            )
        }

        FilledIconButton(
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondary),
            enabled = !state.isPlaying,
            onClick = { onCommand(MetronomeCommand.AdjustBpm(delta = MetronomeDefaults.bpmStep)) }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Plus")
        }
    }
}

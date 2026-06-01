package dev.cankolay.entertempo.wear.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.wear.composables.WearBeatDots
import dev.cankolay.entertempo.wear.viewmodel.WearControlViewModel

@Composable
fun WearControlsView(
    viewModel: WearControlViewModel,
) {
    val state = viewModel.state

    val listState = rememberScalingLazyListState()

    ScreenScaffold(scrollState = listState, edgeButtonSpacing = 8.dp, edgeButton = {
        EdgeButton(onClick = {
            viewModel.dispatch(command = MetronomeCommand.TogglePlayback)
        }) {
            Icon(
                imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (state.isPlaying) "Pause" else "Play"
            )
        }
    }) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            item {
                Text(
                    text = if (state.isPlaying) "Playing on phone" else "Phone control",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            item {
                WearBeatDots(
                    numberOfDots = state.timeSignature.beatsPerMeasure,
                    currentDot = state.currentBeat,
                    isPlaying = state.isPlaying
                )
            }
        }
    }
}

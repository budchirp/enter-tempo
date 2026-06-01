package dev.cankolay.entertempo.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.cankolay.entertempo.composables.BeatDots
import dev.cankolay.entertempo.composables.MetronomeSettingsCard
import dev.cankolay.entertempo.composables.TempoControls
import dev.cankolay.entertempo.viewmodel.MetronomeViewModel

@Composable
fun MetronomeScreen(
    viewModel: MetronomeViewModel = viewModel()
) {
    val state = viewModel.state

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 32.dp, alignment = Alignment.Bottom)
    ) {
        item {
            BeatDots(
                numberOfDots = state.timeSignature.beatsPerMeasure,
                currentDot = state.currentBeat,
                isPlaying = state.isPlaying
            )
        }

        item {
            TempoControls(
                state = state,
                onCommand = viewModel::dispatch
            )
        }

        item {
            MetronomeSettingsCard(
                state = state,
                onCommand = viewModel::dispatch
            )
        }
    }
}

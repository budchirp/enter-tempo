package dev.cankolay.entertempo.wear.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.ScreenScaffold
import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.shared.model.MetronomeDefaults
import dev.cankolay.entertempo.wear.composables.WearTempoAdjustButtons
import dev.cankolay.entertempo.wear.composables.WearTempoReadout
import dev.cankolay.entertempo.wear.viewmodel.WearControlViewModel

@Composable
fun WearBpmView(
    viewModel: WearControlViewModel,
) {
    val state = viewModel.state

    ScreenScaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            WearTempoReadout(
                bpm = state.bpm,
                timeSignature = state.timeSignature
            )


            WearTempoAdjustButtons(
                enabled = !state.isPlaying,
                onDecrease = {
                    viewModel.dispatch(command = MetronomeCommand.AdjustBpm(delta = -MetronomeDefaults.bpmStep))

                },
                onIncrease = {
                    viewModel.dispatch(command = MetronomeCommand.AdjustBpm(delta = MetronomeDefaults.bpmStep))

                }
            )
        }
    }
}

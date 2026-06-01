package dev.cankolay.entertempo.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.shared.model.MetronomeState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeSettingsCard(
    state: MetronomeState,
    onCommand: (MetronomeCommand) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(value = false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BpmValueButton(bpm = state.bpm)

                TimeSignatureButton(
                    timeSignature = state.timeSignature,
                    enabled = !state.isPlaying,
                    onClick = { showSheet = true }
                )
            }

            BpmSlider(
                bpm = state.bpm,
                enabled = !state.isPlaying,
                onBpmChange = { bpm -> onCommand(MetronomeCommand.SetBpm(bpm = bpm)) }
            )
        }
    }

    if (showSheet) {
        TimeSignaturePickerSheet(
            sheetState = sheetState,
            timeSignatures = state.availableTimeSignatures,
            selectedTimeSignature = state.timeSignature,
            onDismissRequest = { showSheet = false },
            onTimeSignatureSelected = { timeSignature ->
                onCommand(MetronomeCommand.SetTimeSignature(timeSignature = timeSignature))

                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showSheet = false
                    }
                }
            }
        )
    }
}

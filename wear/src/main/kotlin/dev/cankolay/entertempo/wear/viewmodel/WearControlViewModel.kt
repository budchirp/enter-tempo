package dev.cankolay.entertempo.wear.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.shared.model.MetronomeDefaults
import dev.cankolay.entertempo.shared.model.MetronomeState
import dev.cankolay.entertempo.wear.model.WearCommandClient
import dev.cankolay.entertempo.wear.model.WearStateStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WearControlViewModel(application: Application) : AndroidViewModel(application) {
    private val commandClient = WearCommandClient(context = application.applicationContext)

    var state by mutableStateOf(value = MetronomeState())
        private set

    init {
        WearStateStore.initialize(context = application.applicationContext)

        WearStateStore.state
            .onEach { state = it }
            .launchIn(viewModelScope)
    }

    fun dispatch(command: MetronomeCommand) {
        WearStateStore.update(state = reduce(state = state, command = command))
        commandClient.send(command = command)
    }

    private fun reduce(state: MetronomeState, command: MetronomeCommand): MetronomeState {
        return when (command) {
            MetronomeCommand.Play -> state.copy(isPlaying = true)
            MetronomeCommand.Stop -> state.copy(isPlaying = false)
            MetronomeCommand.TogglePlayback -> state.copy(isPlaying = !state.isPlaying)
            is MetronomeCommand.AdjustBpm -> state.copy(bpm = clampBpm(bpm = state.bpm + command.delta))
            is MetronomeCommand.SetBpm -> state.copy(bpm = clampBpm(bpm = command.bpm))
            is MetronomeCommand.SetTimeSignature -> state.copy(timeSignature = command.timeSignature)
        }
    }

    private fun clampBpm(bpm: Int): Int {
        return bpm.coerceIn(
            minimumValue = MetronomeDefaults.minBpm,
            maximumValue = MetronomeDefaults.maxBpm
        )
    }
}

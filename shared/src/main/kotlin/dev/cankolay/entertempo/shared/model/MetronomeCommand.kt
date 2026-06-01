package dev.cankolay.entertempo.shared.model

sealed interface MetronomeCommand {
    data object Play : MetronomeCommand
    data object Stop : MetronomeCommand
    data object TogglePlayback : MetronomeCommand
    data class SetBpm(val bpm: Int) : MetronomeCommand
    data class AdjustBpm(val delta: Int) : MetronomeCommand
    data class SetTimeSignature(val timeSignature: TimeSignature) : MetronomeCommand
}

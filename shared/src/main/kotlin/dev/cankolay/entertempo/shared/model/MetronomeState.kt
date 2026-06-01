package dev.cankolay.entertempo.shared.model

data class MetronomeState(
    val bpm: Int = MetronomeDefaults.defaultBpm,
    val isPlaying: Boolean = false,
    val timeSignature: TimeSignature = MetronomeDefaults.defaultTimeSignature,
    val currentBeat: Int = 0,
    val availableTimeSignatures: List<TimeSignature> = MetronomeDefaults.timeSignatures
)

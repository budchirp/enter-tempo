package dev.cankolay.entertempo.shared.model

object MetronomeDefaults {
    const val minBpm = 20
    const val maxBpm = 320
    const val bpmStep = 1
    const val defaultBpm = 120

    val timeSignatures = listOf(
        TimeSignature(beatsPerMeasure = 4, noteValue = 4),
        TimeSignature(beatsPerMeasure = 2, noteValue = 4),
        TimeSignature(beatsPerMeasure = 3, noteValue = 4),
        TimeSignature(beatsPerMeasure = 5, noteValue = 4),
        TimeSignature(beatsPerMeasure = 6, noteValue = 8),
        TimeSignature(beatsPerMeasure = 7, noteValue = 8)
    )

    val defaultTimeSignature = timeSignatures.first()
}

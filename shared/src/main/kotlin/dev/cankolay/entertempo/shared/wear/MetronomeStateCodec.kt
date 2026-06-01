package dev.cankolay.entertempo.shared.wear

import dev.cankolay.entertempo.shared.model.MetronomeState
import dev.cankolay.entertempo.shared.model.TimeSignature

object MetronomeStateCodec {
    fun encode(state: MetronomeState): ByteArray {
        return listOf(
            state.bpm,
            state.isPlaying,
            state.currentBeat,
            state.timeSignature.beatsPerMeasure,
            state.timeSignature.noteValue
        ).joinToString(separator = "|")
            .encodeToByteArray()
    }

    fun decode(data: ByteArray): MetronomeState? {
        val parts = data.decodeToString().split(delimiters = arrayOf("|"))
        val bpm = parts.getOrNull(index = 0)?.toIntOrNull() ?: return null
        val isPlaying = parts.getOrNull(index = 1)?.toBooleanStrictOrNull() ?: return null
        val currentBeat = parts.getOrNull(index = 2)?.toIntOrNull() ?: return null
        val beatsPerMeasure = parts.getOrNull(index = 3)?.toIntOrNull() ?: return null
        val noteValue = parts.getOrNull(index = 4)?.toIntOrNull() ?: return null

        return MetronomeState(
            bpm = bpm,
            isPlaying = isPlaying,
            currentBeat = currentBeat,
            timeSignature = TimeSignature(
                beatsPerMeasure = beatsPerMeasure,
                noteValue = noteValue
            )
        )
    }
}

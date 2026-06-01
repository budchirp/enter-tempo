package dev.cankolay.entertempo.shared.wear

import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.shared.model.TimeSignature

object MetronomeCommandCodec {
    private const val play = "play"
    private const val stop = "stop"
    private const val toggle = "toggle"
    private const val setBpm = "set_bpm"
    private const val adjustBpm = "adjust_bpm"
    private const val setTimeSignature = "set_time_signature"

    fun encode(command: MetronomeCommand): ByteArray {
        return when (command) {
            MetronomeCommand.Play -> play
            MetronomeCommand.Stop -> stop
            MetronomeCommand.TogglePlayback -> toggle
            is MetronomeCommand.SetBpm -> "$setBpm|${command.bpm}"
            is MetronomeCommand.AdjustBpm -> "$adjustBpm|${command.delta}"
            is MetronomeCommand.SetTimeSignature -> {
                "$setTimeSignature|${command.timeSignature.beatsPerMeasure}|${command.timeSignature.noteValue}"
            }
        }.encodeToByteArray()
    }

    fun decode(data: ByteArray): MetronomeCommand? {
        val parts = data.decodeToString().split("|")

        return when (parts.firstOrNull()) {
            play -> MetronomeCommand.Play
            stop -> MetronomeCommand.Stop
            toggle -> MetronomeCommand.TogglePlayback
            setBpm -> parts.getOrNull(index = 1)?.toIntOrNull()?.let { bpm ->
                MetronomeCommand.SetBpm(bpm = bpm)
            }

            adjustBpm -> parts.getOrNull(index = 1)?.toIntOrNull()?.let { delta ->
                MetronomeCommand.AdjustBpm(delta = delta)
            }

            setTimeSignature -> {
                val beatsPerMeasure = parts.getOrNull(index = 1)?.toIntOrNull()
                val noteValue = parts.getOrNull(index = 2)?.toIntOrNull()
                if (beatsPerMeasure != null && noteValue != null) {
                    MetronomeCommand.SetTimeSignature(
                        timeSignature = TimeSignature(
                            beatsPerMeasure = beatsPerMeasure,
                            noteValue = noteValue
                        )
                    )
                } else {
                    null
                }
            }

            else -> null
        }
    }
}

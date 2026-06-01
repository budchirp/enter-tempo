package dev.cankolay.entertempo.model

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import dev.cankolay.entertempo.R
import dev.cankolay.entertempo.shared.model.TimeSignature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MetronomeAudioPlayer(context: Context) {
    private companion object {
        const val tag = "MetronomeAudioPlayer"
    }

    private var soundPool: SoundPool? = null

    private var clickSoundId: Int = 0
    private var accentClickSoundId: Int = 0

    private var job: Job? = null

    val currentBeat = MutableStateFlow(value = 0)
    val isPlaying = MutableStateFlow(value = false)

    init {
        load(context = context)
    }

    private fun load(context: Context) {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        try {
            clickSoundId = soundPool?.load(context, R.raw.click, 1) ?: 0
            accentClickSoundId = soundPool?.load(context, R.raw.click_accent, 1) ?: 0

            if (clickSoundId == 0 || accentClickSoundId == 0) {
                Log.e(tag, "Could not load one or more click sounds.")
            }
        } catch (e: Exception) {
            Log.e(tag, "Error loading sounds.", e)
        }
    }

    fun start(scope: CoroutineScope, bpm: Int, timeSignature: TimeSignature) {
        if (isPlaying.value) return

        isPlaying.value = true
        currentBeat.value = 0

        job?.cancel()
        job = scope.launch {
            val delayMillis = 60000L / bpm
            while (isPlaying.value) {
                currentBeat.value =
                    if (currentBeat.value >= timeSignature.beatsPerMeasure) 1 else currentBeat.value + 1

                soundPool?.play(
                    if (currentBeat.value == 1 && accentClickSoundId != 0) {
                        accentClickSoundId
                    } else {
                        clickSoundId
                    },
                    1.0f,
                    1.0f,
                    1,
                    0,
                    1.0f
                )

                delay(timeMillis = delayMillis)
            }
        }
    }

    fun reset() {
        currentBeat.value = 0
    }

    fun stop() {
        isPlaying.value = false

        job?.cancel()
        job = null
    }

    fun release() {
        stop()

        soundPool?.release()
        soundPool = null
    }
}

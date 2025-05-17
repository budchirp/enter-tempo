package com.cankolay.entertempo.ui.viewmodels

import android.app.Application
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cankolay.entertempo.R
import com.cankolay.entertempo.ui.data.model.TimeSignature
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MetronomeViewModel(application: Application) : AndroidViewModel(application) {
    object Constants {
        val timeSignatures = listOf(
            TimeSignature(4, 4),
            TimeSignature(2, 4),
            TimeSignature(3, 4),
            TimeSignature(5, 4),
            TimeSignature(6, 8),
            TimeSignature(7, 8),
        )
    }

    var bpm by mutableIntStateOf(120)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var timeSignature by mutableStateOf(Constants.timeSignatures.first())
        private set

    var currentBeat by mutableIntStateOf(0)
        private set

    fun updateBpm(bpm: Int) {
        this.bpm = bpm
    }

    fun togglePlaying() {
        isPlaying = !isPlaying

        if (isPlaying) {
            startMetronome()
        } else {
            stopMetronome()
        }
    }

    fun updateTimeSignature(timeSignature: TimeSignature) {
        this.timeSignature = timeSignature

        currentBeat = 0
    }

    private var soundPool: SoundPool? = null

    private var clickSoundId: Int = 0
    private var accentClickSoundId: Int = 0

    private var metronomeJob: Job? = null

    init {
        loadSounds(application.applicationContext)
    }

    private fun loadSounds(context: Context) {
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
            accentClickSoundId =
                soundPool?.load(context, R.raw.click_accent, 1) ?: 0

            if (clickSoundId == 0 || accentClickSoundId == 0) {
                println("Error: Could not load one or more click sounds.")
            }
        } catch (e: Exception) {
            println("Error loading sounds: ${e.localizedMessage}")
        }
    }

    private fun startMetronome() {
        currentBeat = 0

        metronomeJob?.cancel()
        metronomeJob = viewModelScope.launch {
            val delayMillis = 60000L / bpm
            while (isPlaying) {
                currentBeat =
                    if (currentBeat >= timeSignature.beatsPerMeasure) 1 else currentBeat + 1

                val soundToPlay = if (currentBeat == 1 && accentClickSoundId != 0) {
                    accentClickSoundId
                } else {
                    clickSoundId
                }
                soundPool?.play(soundToPlay, 1.0f, 1.0f, 1, 0, 1.0f)
                delay(delayMillis)
            }
        }
    }

    private fun stopMetronome() {
        metronomeJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()

        soundPool?.release()
        soundPool = null

        stopMetronome()
    }
}

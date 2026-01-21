package dev.cankolay.entertempo.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.cankolay.entertempo.ui.model.TimeSignature
import dev.cankolay.entertempo.ui.service.MetronomeService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

    private val metronomeService = MetronomeService(application.applicationContext)

    var bpm by mutableIntStateOf(value = 120)
        private set

    var isPlaying by mutableStateOf(value = false)
        private set

    var timeSignature by mutableStateOf(value = Constants.timeSignatures.first())
        private set

    var currentBeat by mutableIntStateOf(value = 0)
        private set

    init {
        metronomeService.isPlaying
            .onEach { isPlaying = it }
            .launchIn(viewModelScope)

        metronomeService.currentBeat
            .onEach { currentBeat = it }
            .launchIn(viewModelScope)
    }

    fun updateBpm(bpm: Int) {
        this.bpm = bpm

        if (isPlaying) {
            metronomeService.stop()
            metronomeService.start(viewModelScope, this.bpm, timeSignature)
        }
    }

    fun updateTimeSignature(timeSignature: TimeSignature) {
        this.timeSignature = timeSignature

        metronomeService.reset()
    }

    fun toggle() {
        if (isPlaying) {
            metronomeService.stop()
        } else {
            metronomeService.start(viewModelScope, bpm, timeSignature)
        }
    }

    override fun onCleared() {
        super.onCleared()

        metronomeService.release()
    }
}

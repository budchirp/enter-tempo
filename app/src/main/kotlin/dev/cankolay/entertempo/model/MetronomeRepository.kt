package dev.cankolay.entertempo.model

import android.content.Context
import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.shared.model.MetronomeDefaults
import dev.cankolay.entertempo.shared.model.MetronomeState
import dev.cankolay.entertempo.shared.model.TimeSignature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object MetronomeRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(value = MetronomeState())

    private var audioPlayer: MetronomeAudioPlayer? = null
    private var statePublisher: WearStatePublisher? = null

    val state: Flow<MetronomeState> = _state.asStateFlow()

    fun initialize(context: Context) {
        val applicationContext = context.applicationContext
        statePublisher = statePublisher ?: WearStatePublisher(context = applicationContext)
        audioPlayer(context = applicationContext)
        publishState()
    }

    fun dispatch(context: Context, command: MetronomeCommand) {
        val applicationContext = context.applicationContext
        initialize(context = applicationContext)

        val metronomeAudioPlayer = audioPlayer(context = applicationContext)

        when (command) {
            MetronomeCommand.Play -> play(audioPlayer = metronomeAudioPlayer)
            MetronomeCommand.Stop -> metronomeAudioPlayer.stop()
            MetronomeCommand.TogglePlayback -> toggle(audioPlayer = metronomeAudioPlayer)
            is MetronomeCommand.AdjustBpm -> updateBpm(
                audioPlayer = metronomeAudioPlayer,
                bpm = _state.value.bpm + command.delta
            )

            is MetronomeCommand.SetBpm -> updateBpm(
                audioPlayer = metronomeAudioPlayer,
                bpm = command.bpm
            )

            is MetronomeCommand.SetTimeSignature -> updateTimeSignature(
                audioPlayer = metronomeAudioPlayer,
                timeSignature = command.timeSignature
            )
        }
    }

    @Synchronized
    private fun audioPlayer(context: Context): MetronomeAudioPlayer {
        val existingAudioPlayer = audioPlayer
        if (existingAudioPlayer != null) return existingAudioPlayer

        return MetronomeAudioPlayer(context = context).also { metronomeAudioPlayer ->
            audioPlayer = metronomeAudioPlayer

            metronomeAudioPlayer.isPlaying
                .onEach { isPlaying -> updateState { state -> state.copy(isPlaying = isPlaying) } }
                .launchIn(scope)

            metronomeAudioPlayer.currentBeat
                .onEach { currentBeat -> updateState { state -> state.copy(currentBeat = currentBeat) } }
                .launchIn(scope)
        }
    }

    private fun updateBpm(audioPlayer: MetronomeAudioPlayer, bpm: Int) {
        updateState { state ->
            state.copy(
                bpm = bpm.coerceIn(
                    minimumValue = MetronomeDefaults.minBpm,
                    maximumValue = MetronomeDefaults.maxBpm
                )
            )
        }

        if (_state.value.isPlaying) {
            audioPlayer.stop()
            audioPlayer.start(
                scope = scope,
                bpm = _state.value.bpm,
                timeSignature = _state.value.timeSignature
            )
        }
    }

    private fun updateTimeSignature(audioPlayer: MetronomeAudioPlayer, timeSignature: TimeSignature) {
        updateState { state -> state.copy(timeSignature = timeSignature) }

        audioPlayer.reset()
    }

    private fun toggle(audioPlayer: MetronomeAudioPlayer) {
        if (_state.value.isPlaying) {
            audioPlayer.stop()
        } else {
            play(audioPlayer = audioPlayer)
        }
    }

    private fun play(audioPlayer: MetronomeAudioPlayer) {
        audioPlayer.start(
            scope = scope,
            bpm = _state.value.bpm,
            timeSignature = _state.value.timeSignature
        )
    }

    private inline fun updateState(transform: (MetronomeState) -> MetronomeState) {
        _state.value = transform(_state.value)
        publishState()
    }

    private fun publishState() {
        statePublisher?.publish(state = _state.value)
    }
}

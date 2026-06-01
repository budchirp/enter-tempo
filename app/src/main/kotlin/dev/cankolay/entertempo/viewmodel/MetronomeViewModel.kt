package dev.cankolay.entertempo.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.cankolay.entertempo.model.MetronomeRepository
import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.shared.model.MetronomeState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MetronomeViewModel(application: Application) : AndroidViewModel(application) {
    var state by mutableStateOf(value = MetronomeState())
        private set

    init {
        MetronomeRepository.initialize(context = application.applicationContext)

        MetronomeRepository.state
            .onEach { state = it }
            .launchIn(viewModelScope)
    }

    fun dispatch(command: MetronomeCommand) {
        MetronomeRepository.dispatch(context = getApplication(), command = command)
    }
}

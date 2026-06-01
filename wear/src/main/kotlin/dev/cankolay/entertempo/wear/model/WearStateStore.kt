package dev.cankolay.entertempo.wear.model

import android.content.Context
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import dev.cankolay.entertempo.shared.model.MetronomeState
import dev.cankolay.entertempo.shared.wear.MetronomeControlPaths
import dev.cankolay.entertempo.shared.wear.MetronomeStateCodec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object WearStateStore {
    private val _state = MutableStateFlow(value = MetronomeState())
    private var initialized = false

    val state: StateFlow<MetronomeState> = _state.asStateFlow()

    fun initialize(context: Context) {
        if (initialized) return

        initialized = true
        loadLatestState(context = context.applicationContext)
    }

    fun update(state: MetronomeState) {
        _state.value = state
    }

    private fun loadLatestState(context: Context) {
        Wearable.getDataClient(context).dataItems.addOnSuccessListener { dataItems ->
            dataItems.use { items ->
                items
                    .filter { dataItem -> dataItem.uri.path == MetronomeControlPaths.statePath }
                    .mapNotNull { dataItem ->
                        DataMapItem.fromDataItem(dataItem)
                            .dataMap
                            .getByteArray(MetronomeControlPaths.stateKey)
                    }
                    .mapNotNull { encodedState -> MetronomeStateCodec.decode(data = encodedState) }
                    .lastOrNull()
                    ?.let { state -> update(state = state) }
            }
        }
    }
}

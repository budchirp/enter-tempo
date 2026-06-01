package dev.cankolay.entertempo.wear.model

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dev.cankolay.entertempo.shared.wear.MetronomeControlPaths
import dev.cankolay.entertempo.shared.wear.MetronomeStateCodec

class WearStateListenerService : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { dataEvent ->
            if (dataEvent.type != DataEvent.TYPE_CHANGED) return@forEach
            if (dataEvent.dataItem.uri.path != MetronomeControlPaths.statePath) return@forEach

            val dataMap = DataMapItem.fromDataItem(dataEvent.dataItem).dataMap
            val encodedState = dataMap.getByteArray(MetronomeControlPaths.stateKey) ?: return@forEach
            val state = MetronomeStateCodec.decode(data = encodedState) ?: return@forEach

            WearStateStore.update(state = state)
        }
    }
}

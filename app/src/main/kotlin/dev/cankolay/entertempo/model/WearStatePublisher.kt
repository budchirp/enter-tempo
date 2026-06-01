package dev.cankolay.entertempo.model

import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dev.cankolay.entertempo.shared.model.MetronomeState
import dev.cankolay.entertempo.shared.wear.MetronomeControlPaths
import dev.cankolay.entertempo.shared.wear.MetronomeStateCodec

class WearStatePublisher(context: Context) {
    private val dataClient = Wearable.getDataClient(context)

    fun publish(state: MetronomeState) {
        val request = PutDataMapRequest.create(MetronomeControlPaths.statePath)
            .apply {
                dataMap.putByteArray(
                    MetronomeControlPaths.stateKey,
                    MetronomeStateCodec.encode(state = state)
                )
                dataMap.putLong(MetronomeControlPaths.updatedAtKey, System.currentTimeMillis())
            }
            .asPutDataRequest()
            .setUrgent()

        dataClient.putDataItem(request)
    }
}

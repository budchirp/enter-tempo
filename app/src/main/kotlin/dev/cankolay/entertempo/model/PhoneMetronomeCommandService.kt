package dev.cankolay.entertempo.model

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import dev.cankolay.entertempo.shared.wear.MetronomeCommandCodec
import dev.cankolay.entertempo.shared.wear.MetronomeControlPaths

class PhoneMetronomeCommandService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path != MetronomeControlPaths.commandPath) return

        val command = MetronomeCommandCodec.decode(data = messageEvent.data) ?: return
        MetronomeRepository.dispatch(context = applicationContext, command = command)
    }
}

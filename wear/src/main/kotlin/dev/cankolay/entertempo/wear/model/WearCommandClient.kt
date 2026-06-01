package dev.cankolay.entertempo.wear.model

import android.content.Context
import com.google.android.gms.wearable.Wearable
import dev.cankolay.entertempo.shared.model.MetronomeCommand
import dev.cankolay.entertempo.shared.wear.MetronomeCommandCodec
import dev.cankolay.entertempo.shared.wear.MetronomeControlPaths

class WearCommandClient(context: Context) {
    private val nodeClient = Wearable.getNodeClient(context)
    private val messageClient = Wearable.getMessageClient(context)

    fun send(command: MetronomeCommand) {
        val payload = MetronomeCommandCodec.encode(command = command)

        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            nodes.forEach { node ->
                messageClient.sendMessage(node.id, MetronomeControlPaths.commandPath, payload)
            }
        }
    }
}

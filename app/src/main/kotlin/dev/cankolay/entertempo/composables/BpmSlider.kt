package dev.cankolay.entertempo.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.cankolay.entertempo.shared.model.MetronomeDefaults

@Composable
fun BpmSlider(
    bpm: Int,
    enabled: Boolean,
    onBpmChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Slider(
        modifier = modifier.fillMaxWidth(),
        value = bpm.toFloat(),
        valueRange = MetronomeDefaults.minBpm.toFloat()..MetronomeDefaults.maxBpm.toFloat(),
        enabled = enabled,
        onValueChange = { onBpmChange(it.toInt()) }
    )
}

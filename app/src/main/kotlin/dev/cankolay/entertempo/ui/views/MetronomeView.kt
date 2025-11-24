package dev.cankolay.entertempo.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.cankolay.entertempo.ui.viewmodels.MetronomeViewModel

@Composable
fun MetronomeView(viewModel: MetronomeViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 64.dp, alignment = Alignment.Bottom)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BeatDots(
                numberOfDots = viewModel.timeSignature.beatsPerMeasure,
                isPlaying = viewModel.isPlaying,
                currentDot = viewModel.currentBeat
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 32.dp, alignment = Alignment.Bottom)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${viewModel.bpm}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    var showMenu by remember { mutableStateOf(false) }
                    Button(onClick = {
                        showMenu = true
                    }, enabled = !viewModel.isPlaying) {
                        Text(text = viewModel.timeSignature.toString())
                    }

                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        MetronomeViewModel.Constants.timeSignatures.forEach {
                            DropdownMenuItem(text = { Text(text = it.toString()) }, onClick = {
                                viewModel.updateTimeSignature(it)

                                showMenu = false
                            })
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 32.dp, alignment = Alignment.Bottom)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 32.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.updateBpm(maxOf(viewModel.bpm - 1, 40))
                }, enabled = !viewModel.isPlaying) {
                    Icon(Icons.Default.Remove, contentDescription = "Minus")
                }

                LargeFloatingActionButton(onClick = {
                    viewModel.togglePlaying()
                }, shape = CircleShape) {
                    Icon(
                        if (viewModel.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play"
                    )
                }

                IconButton(onClick = {
                    viewModel.updateBpm(minOf(viewModel.bpm + 1, 240))
                }, enabled = !viewModel.isPlaying) {
                    Icon(Icons.Default.Add, contentDescription = "Plus")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(value = viewModel.bpm.toFloat(), onValueChange = {
                    viewModel.updateBpm(it.toInt())
                }, valueRange = 40f..240f, enabled = !viewModel.isPlaying)
            }
        }
    }
}

@Composable
fun BeatDots(
    numberOfDots: Int,
    currentDot: Int,
    isPlaying: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..numberOfDots) {
            val color =
                if (isPlaying && i == currentDot) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.3f
                )

            Box(
                modifier = Modifier
                    .size(size = 24.dp)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}
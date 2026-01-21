package dev.cankolay.entertempo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.cankolay.entertempo.ui.viewmodel.MetronomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MetronomeView(viewModel: MetronomeViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 32.dp, alignment = Alignment.Bottom)
    ) {
        item {
            BeatDots(
                numberOfDots = viewModel.timeSignature.beatsPerMeasure,
                isPlaying = viewModel.isPlaying,
                currentDot = viewModel.currentBeat
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 32.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    enabled = !viewModel.isPlaying,
                    onClick = {
                        viewModel.updateBpm(bpm = maxOf(a = viewModel.bpm - 1, 40))
                    }
                ) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus")
                }

                FilledIconButton(
                    modifier = Modifier.size(height = 96.dp, width = 160.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.extraExtraLarge,
                    onClick = {
                        viewModel.toggle()
                    },
                ) {
                    Icon(
                        imageVector = if (viewModel.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play"
                    )
                }

                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    enabled = !viewModel.isPlaying,
                    onClick = {
                        viewModel.updateBpm(bpm = minOf(a = viewModel.bpm + 1, 240))
                    },
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Plus")
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
                            ),
                            shape = MaterialTheme.shapes.medium,
                            onClick = {},
                        ) {
                            Text(
                                text = "${viewModel.bpm}",
                            )
                        }

                        val sheetState = rememberModalBottomSheetState()
                        val coroutineScope = rememberCoroutineScope()

                        var showSheet by remember { mutableStateOf(value = false) }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
                            ),
                            shape = MaterialTheme.shapes.medium,
                            enabled = !viewModel.isPlaying,
                            onClick = {
                                showSheet = true
                            },
                        ) {
                            Text(text = viewModel.timeSignature.toString())
                        }

                        if (showSheet) {
                            ModalBottomSheet(sheetState = sheetState, onDismissRequest = {
                                showSheet = false
                            }, containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(space = 16.dp)
                                ) {
                                    items(items = MetronomeViewModel.Constants.timeSignatures) { timeSignature ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                            shape = MaterialTheme.shapes.large,
                                            onClick = {
                                                viewModel.updateTimeSignature(timeSignature = timeSignature)

                                                coroutineScope.launch {
                                                    sheetState.hide()
                                                }.invokeOnCompletion {
                                                    if (!sheetState.isVisible) {
                                                        showSheet = false
                                                    }
                                                }
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(all = 16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(text = timeSignature.toString())

                                                if (viewModel.timeSignature == timeSignature) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Check"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.bpm.toFloat(),
                        valueRange = 20f..320f,
                        enabled = !viewModel.isPlaying,
                        onValueChange = {
                            viewModel.updateBpm(bpm = it.toInt())
                        },
                    )
                }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..numberOfDots) {
            Box(
                modifier = Modifier
                    .size(size = 24.dp)
                    .background(
                        color = if (isPlaying && i == currentDot) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.3f
                        ), shape = CircleShape
                    )
            )
        }
    }
}
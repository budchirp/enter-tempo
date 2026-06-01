package dev.cankolay.entertempo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.cankolay.entertempo.screen.MetronomeScreen
import dev.cankolay.entertempo.theme.AppTheme

@Composable
fun AppUI() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
                MetronomeScreen()
            }
        }
}
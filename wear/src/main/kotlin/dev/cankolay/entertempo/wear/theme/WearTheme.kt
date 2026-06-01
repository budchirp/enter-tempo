package dev.cankolay.entertempo.wear.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.dynamicColorScheme

@Composable
fun WearTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current

    MaterialTheme(
        content = content,
        colorScheme = dynamicColorScheme(context = context) ?: MaterialTheme.colorScheme
    )
}

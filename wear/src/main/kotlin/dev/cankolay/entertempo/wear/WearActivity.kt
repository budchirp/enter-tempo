package dev.cankolay.entertempo.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.cankolay.entertempo.wear.theme.WearTheme

class WearActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearTheme {
                WearUI()
            }
        }
    }
}

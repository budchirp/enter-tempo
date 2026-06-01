package dev.cankolay.entertempo.wear

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.pager.PagerDefaults
import androidx.wear.compose.foundation.pager.VerticalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.material3.AnimatedPage
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.PagerScaffoldDefaults
import androidx.wear.compose.material3.VerticalPagerScaffold
import dev.cankolay.entertempo.wear.screen.WearBpmView
import dev.cankolay.entertempo.wear.screen.WearControlsView
import dev.cankolay.entertempo.wear.viewmodel.WearControlViewModel

@Composable
fun WearUI(viewModel: WearControlViewModel = viewModel()) {
    val state = rememberPagerState(pageCount = { 2 })

    AppScaffold {
        VerticalPagerScaffold(pagerState = state) {
            VerticalPager(
                state = state,
                flingBehavior =
                    PagerDefaults.snapFlingBehavior(
                        state = state,
                        maxFlingPages = 1,
                        snapPositionalThreshold = PagerScaffoldDefaults.HighSnapPositionalThreshold,
                        snapAnimationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
                    ),
                rotaryScrollableBehavior = RotaryScrollableDefaults.snapBehavior(pagerState = state),
            ) { page ->
                when (page) {
                    0 -> AnimatedPage(pageIndex = page, pagerState = state) {
                        WearControlsView(viewModel = viewModel)
                    }

                    else -> AnimatedPage(pageIndex = page, pagerState = state) {
                        WearBpmView(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

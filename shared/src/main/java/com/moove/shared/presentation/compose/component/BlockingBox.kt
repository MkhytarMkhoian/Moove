package com.moove.shared.presentation.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moove.design_system.compose.AppTheme
import com.moove.design_system.compose.CircularProgressIndicator
import kotlinx.coroutines.delay
import kotlin.math.max

@Composable
fun BlockingBox(
    modifier: Modifier = Modifier,
    blocked: Boolean,
    blockSilentlyImmediately: Boolean = true,
    delayBeforeBlock: Long = BlockingBoxDefaults.longDelay,
    minTimeToShow: Long = BlockingBoxDefaults.longDelay,
    blockingSurface: @Composable BoxScope.() -> Unit = defaultBlockingSurface,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        content()
        var showSurface by remember { mutableStateOf(false) }
        var surfaceShownAt by remember { mutableStateOf(0L) }
        LaunchedEffect(key1 = blocked) {
            if (blocked) {
                delay(delayBeforeBlock)
                surfaceShownAt = System.currentTimeMillis()
                showSurface = true
            } else if (showSurface) {
                val shownFor = System.currentTimeMillis() - surfaceShownAt
                delay(max(0, minTimeToShow - shownFor))
                showSurface = false
            }
        }
        if (blocked && blockSilentlyImmediately) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) { /*do nothing*/ }
            )
        }
        AnimatedVisibility(
            visible = showSurface,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            blockingSurface()
        }
    }
}

object BlockingBoxDefaults {
    const val zeroDelay = 0L
    const val shortDelay = 300L
    const val mediumDelay = 750L
    const val longDelay = 1000L
}

internal val defaultBlockingSurface: @Composable BoxScope.() -> Unit = {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppTheme.colors.material.surface.copy(alpha = 0.6f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

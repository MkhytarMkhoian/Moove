package com.moove.shared.presentation.compose.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.moove.design_system.compose.AppTheme
import io.github.mkhytarmkhoian.herald.compose.LocalEventTrackerService
import org.koin.compose.koinInject

fun ComposeView.setAppComposeContent(strategy: ViewCompositionStrategy? = null, content: @Composable () -> Unit) {
    strategy?.also(::setViewCompositionStrategy)
    setContent {
        // The one place DI meets Compose: herald-compose reads the tracker from this local.
        CompositionLocalProvider(LocalEventTrackerService provides koinInject()) {
            AppTheme(content = content)
        }
    }
}

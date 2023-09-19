package com.moove.shared.presentation.compose.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.moove.design_system.compose.AppTheme

fun ComposeView.setAppComposeContent(strategy: ViewCompositionStrategy? = null, content: @Composable () -> Unit) {
    strategy?.also(::setViewCompositionStrategy)
    setContent {
        AppTheme(content = content)
    }
}

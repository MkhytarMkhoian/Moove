package com.moove.design_system.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.moove.design_system.compose.moove.MooveTheme

/** Shared application theme */
@Composable
fun AppTheme(theme: Theme = Theme.MOOVE, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalTheme provides theme,
    ) {
        when (AppTheme.theme) {
            Theme.MOOVE -> MooveTheme(content)
        }
    }
}

enum class Theme {
    MOOVE
}

val LocalTheme = staticCompositionLocalOf { Theme.MOOVE }

object AppTheme {
    val theme: Theme
        @Composable
        get() = LocalTheme.current
    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
    val shapes: AppShapes
        @Composable
        get() = LocalAppShapes.current
}

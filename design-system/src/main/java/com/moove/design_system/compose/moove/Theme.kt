package com.moove.design_system.compose.moove

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.moove.design_system.compose.LocalAppColors
import com.moove.design_system.compose.LocalAppShapes
import com.moove.design_system.compose.LocalAppTypography

@Composable
internal fun MooveTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = mooveColors.material,
        typography = mooveTypography.material,
        shapes = mooveShapes.material,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MooveColors.Text,
            LocalAppTypography provides mooveTypography,
            LocalAppColors provides mooveColors,
            LocalAppShapes provides mooveShapes,
            content = content
        )
    }
}

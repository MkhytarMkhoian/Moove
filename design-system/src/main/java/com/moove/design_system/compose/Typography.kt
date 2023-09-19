package com.moove.design_system.compose

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class AppTypography(
    val material: Typography,
    val appBar: TopAppBarTypography,
)

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        material = Typography(),
        appBar = TopAppBarTypography()
    )
}

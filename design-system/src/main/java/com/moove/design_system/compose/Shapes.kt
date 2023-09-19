package com.moove.design_system.compose

import androidx.compose.material.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class AppShapes(
    val material: Shapes,
)

val LocalAppShapes = staticCompositionLocalOf {
    AppShapes(
        material = Shapes(),
    )
}

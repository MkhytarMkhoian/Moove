package com.moove.design_system.compose

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class AppColors(
    val material: Colors,
    val neutral: NeutralColors,
    val buttons: ButtonsColors,
)

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        material = lightColors(),
        neutral = NeutralColors(),
        buttons = ButtonsColors(),
    )
}

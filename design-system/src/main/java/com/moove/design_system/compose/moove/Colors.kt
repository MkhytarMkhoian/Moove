package com.moove.design_system.compose.moove

import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.moove.design_system.compose.AppColors

internal val mooveColors: AppColors
    @Composable
    get() {
        val material = lightColors(
            primary = MooveColors.FloytGreen,
            primaryVariant = MooveColors.Mint,
            onPrimary = MooveColors.White,
            secondary = MooveColors.FloytGreen,
            onSecondary = MooveColors.White,
            surface = MooveColors.White,
            onSurface = MooveColors.Text,
            background = MooveColors.White,
            onBackground = MooveColors.Text,
            error = MooveColors.AccentRed,
            onError = MooveColors.White,
        )
        return AppColors(
            material = material,
            neutral = mooveNeutralColors,
            buttons = mooveButtonsColors,
        )
    }

internal object MooveColors {
    val FloytGreen = Color(0xFF00977D)
    val Mint = Color(0xFF6EC2B7)
    val Mint30 = Color(0xFFD4EDE9)
    val Rose = Color(0xFFE4A5A3)
    val Orange = Color(0xFFFF4B19)
    val AccentGreen = Color(0xFF3C9307)
    val AccentYellow = Color(0xFFFFA800)
    val AccentRed = Color(0xFFD63F24)
    val AccentLightGreen = Color(0xFF75B21B)
    val AccentBlue = Color(0xFF3694BA)
    val Grey01 = Color(0xFF5A5A5A)
    val Grey02 = Color(0xFF909090)
    val Grey03 = Color(0xFFC8C8C8)
    val Grey04 = Color(0xFFE8E8E8)
    val Grey05 = Color(0xFFF3F3F3)
    val Black = Color(0xFF000000)
    val White = Color(0xFFFFFFFF)
    val Text = Color(0xFF222222)
}

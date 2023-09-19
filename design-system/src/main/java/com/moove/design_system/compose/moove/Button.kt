package com.moove.design_system.compose.moove

import androidx.compose.runtime.Composable
import com.moove.design_system.compose.ButtonsColors
import com.moove.design_system.compose.PrimaryButtonColors

private object MooveButtonsDefaults {
    val mainColor = MooveColors.FloytGreen
    val contentColor = MooveColors.White
    val disabledColor = MooveColors.Grey05
    val disabledContentColor = MooveColors.Grey03
}

internal val mooveButtonsColors: ButtonsColors
    @Composable
    get() = ButtonsColors(
        primary = PrimaryButtonColors(
            background = MooveButtonsDefaults.mainColor,
            content = MooveButtonsDefaults.contentColor,
            disabledBackground = MooveButtonsDefaults.disabledColor,
            disabledContent = MooveButtonsDefaults.disabledContentColor,
        ),
    )

@file:SuppressLint("ModifierParameter")

package com.moove.design_system.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ButtonsColors(
    val primary: PrimaryButtonColors = PrimaryButtonColors(),
)

@Immutable
data class PrimaryButtonColors(
    val background: Color = Color.Unspecified,
    val content: Color = Color.Unspecified,
    val disabledBackground: Color = Color.Unspecified,
    val disabledContent: Color = Color.Unspecified,
)

object AppButtonDefaults {

    val Modifier = androidx.compose.ui.Modifier
        .defaultMinSize(minWidth = 328.dp)
        .heightIn(min = 56.dp)

    val MinWidth = 36.dp
    val MinHeight = 36.dp

    @Composable
    fun primaryButtonColors(
        backgroundColor: Color = AppTheme.colors.buttons.primary.background,
        contentColor: Color = AppTheme.colors.buttons.primary.content,
        disabledBackgroundColor: Color = AppTheme.colors.buttons.primary.disabledBackground,
        disabledContentColor: Color = AppTheme.colors.buttons.primary.disabledContent,
    ) = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun defaultElevation(
        defaultElevation: Dp = 0.dp,
        pressedElevation: Dp = 8.dp,
        disabledElevation: Dp = 0.dp
    ) = ButtonDefaults.elevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        disabledElevation = disabledElevation
    )
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = AppButtonDefaults.Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = AppButtonDefaults.defaultElevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = AppButtonDefaults.primaryButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) = Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    content = content
)

/** Copy of the [androidx.compose.material.Button] but with different min width/height */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.defaultMinSize(
        minWidth = AppButtonDefaults.MinWidth,
        minHeight = AppButtonDefaults.MinHeight
    ),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = AppButtonDefaults.defaultElevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp,
        interactionSource = interactionSource
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.button
            ) {
                Row(
                    contentModifier.padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3_XL)
@Composable
private fun ButtonsPreview() {
    val scrollState = rememberScrollState()
    AppTheme {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(Spacing.L),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryButton(onClick = {}) { Text(text = "Primary Button") }
            Spacer(Modifier.height(Spacing.M))
            PrimaryButton(onClick = {}, enabled = false) { Text(text = "Primary Button") }
            Spacer(Modifier.height(Spacing.M))
        }
    }
}

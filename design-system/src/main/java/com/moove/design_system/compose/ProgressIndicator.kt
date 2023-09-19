package com.moove.design_system.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.CircularProgressIndicator as MaterialCircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator as MaterialLinearProgressIndicator

/**
 * @param progress in range (0..1.0)
 */
@Composable
fun LinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.primaryVariant
) = MaterialLinearProgressIndicator(
    progress,
    modifier,
    color,
    backgroundColor
)

@Composable
fun LinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.primaryVariant
) = MaterialLinearProgressIndicator(
    modifier,
    color,
    backgroundColor
)

@Composable
fun CircularProgressIndicator(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) = MaterialCircularProgressIndicator(
    progress,
    modifier,
    color,
    strokeWidth
)

@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) = MaterialCircularProgressIndicator(
    modifier,
    color,
    strokeWidth
)

object ProgressIndicatorDefaults {
    val StrokeWidth = 4.dp
}

@Preview(showBackground = true, device = Devices.PIXEL_3_XL)
@Composable
private fun ProgressIndicatorPreview() {
    val scrollState = rememberScrollState()
    AppTheme {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(Spacing.L),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LinearProgressIndicator(progress = 0.5F)
            Spacer(Modifier.height(Spacing.M))
            CircularProgressIndicator(progress = 0.5F)
            Spacer(Modifier.height(Spacing.M))
            CircularProgressIndicator()
            Spacer(Modifier.height(Spacing.M))
        }
    }
}

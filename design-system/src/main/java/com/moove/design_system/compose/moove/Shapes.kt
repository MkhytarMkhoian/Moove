package com.moove.design_system.compose.moove

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.moove.design_system.compose.AppShapes

internal val mooveShapes: AppShapes
    @Composable
    get() = AppShapes(
        material = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(8.dp),
            large = RoundedCornerShape(8.dp)
        ),
    )

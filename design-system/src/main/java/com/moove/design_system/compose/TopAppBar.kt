package com.moove.design_system.compose

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class TopAppBarTypography(
    val title: TextStyle = TextStyle.Default,
    val titleExpanded: TextStyle = TextStyle.Default,
)

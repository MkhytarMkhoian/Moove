package com.moove.app.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moove.design_system.compose.AppTheme
import com.moove.design_system.compose.Button
import com.moove.design_system.compose.Scaffold
import com.moove.design_system.compose.Spacing

@Composable
fun HomeScreen(
    uiState: HomeState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onRyderClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .clickable(onClick = { onRyderClick() })
                        .padding(horizontal = Spacing.S, vertical = Spacing.S),
                    onClick = onRyderClick
                ) {
                    Text(
                        text = "Go to Ryders list",
                        style = AppTheme.typography.material.h1,
                        maxLines = 1,
                    )
                }
            }
        }
    )
}

@Preview(name = "Home Content", showBackground = true)
@Composable
fun PreviewHomeContent() {
    AppTheme {
        HomeScreen(
            uiState = HomeState(),
            onRyderClick = {}
        )
    }
}
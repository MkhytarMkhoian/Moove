package com.moove.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.moove.design_system.compose.AppTheme
import com.moove.design_system.compose.Button
import com.moove.design_system.compose.Scaffold

@Composable
fun HomeScreen(
    uiState: HomeState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onRyderClick: () -> Unit,
    onMoviesClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = onRyderClick) {
                    Text(
                        text = "Go to Ryders list",
                        style = AppTheme.typography.material.h6,
                        maxLines = 1,
                    )
                }
                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = onMoviesClick,
                ) {
                    Text(
                        text = "Popular Movies",
                        style = AppTheme.typography.material.h6,
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
            onRyderClick = {},
            onMoviesClick = {},
        )
    }
}
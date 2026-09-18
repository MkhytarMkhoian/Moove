package com.moove.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moove.design_system.compose.AppTheme

@Composable
fun HomeScreen(
    uiState: HomeState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onRyderClick: () -> Unit,
    onMoviesClick: () -> Unit,
    onInspectorClick: () -> Unit = {},
    onAnalyticsToggled: (Boolean) -> Unit = {},
    onSignInClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        content = { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
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

                Spacer(modifier = Modifier.height(48.dp))

                AnalyticsControls(
                    uiState = uiState,
                    onInspectorClick = onInspectorClick,
                    onAnalyticsToggled = onAnalyticsToggled,
                    onSignInClick = onSignInClick,
                    onSignOutClick = onSignOutClick,
                )
            }
        }
    )
}

/** The consent switch, the demo identity, and the way into the inspector. */
@Composable
private fun AnalyticsControls(
    uiState: HomeState,
    onInspectorClick: () -> Unit,
    onAnalyticsToggled: (Boolean) -> Unit,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Analytics", style = AppTheme.typography.material.body1)
        Spacer(modifier = Modifier.width(12.dp))
        Switch(checked = uiState.analyticsEnabled, onCheckedChange = onAnalyticsToggled)
    }
    val user = uiState.signedInUserId
    if (user == null) {
        OutlinedButton(modifier = Modifier.padding(top = 8.dp), onClick = onSignInClick) {
            Text(text = "Sign in as ${HomeViewModel.DEMO_USER}")
        }
    } else {
        OutlinedButton(modifier = Modifier.padding(top = 8.dp), onClick = onSignOutClick) {
            Text(text = "Sign out $user")
        }
    }
    OutlinedButton(modifier = Modifier.padding(top = 8.dp), onClick = onInspectorClick) {
        Text(text = "Analytics inspector")
    }
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

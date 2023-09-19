package com.moove.tickets.presentation.list

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.moove.design_system.compose.AppTheme
import com.moove.design_system.compose.Scaffold
import com.moove.shared.presentation.compose.component.ScreenContent
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.shared.presentation.compose.component.isLoading
import com.moove.tickets.presentation.list.component.RyderList
import com.moove.tickets.presentation.list.model.RyderModel
import com.moove.tickets.presentation.list.model.fakeRyderModels

@Composable
fun RyderListScreen(
    uiState: RyderListState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onRyderClick: (RyderModel) -> Unit,
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = com.moove.shared.R.string.select_ryder_title))
                },
                backgroundColor = AppTheme.colors.material.surface,
            )
        },
        content = {
            ScreenContent(
                status = uiState.status,
                forceLoading = uiState.status.isLoading,
            ) {
                RyderList(
                    ryders = uiState.ryders,
                    onClick = onRyderClick
                )
            }
        }
    )
}

@Preview(name = "Ryders Content", showBackground = true)
@Composable
fun PreviewRydersContent() {
    AppTheme {
        RyderListScreen(
            uiState = RyderListState(
                status = ScreenContentStatus.Success,
                ryders = fakeRyderModels,
            ),
            onRyderClick = {}
        )
    }
}
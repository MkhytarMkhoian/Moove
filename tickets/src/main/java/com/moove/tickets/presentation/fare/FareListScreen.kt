package com.moove.tickets.presentation.fare

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
import com.moove.shared.R
import com.moove.shared.presentation.compose.component.ScreenContent
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.shared.presentation.compose.component.isLoading
import com.moove.tickets.presentation.fare.component.FareList
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.fakeFareModels

@Composable
fun FareListScreen(
    uiState: FareListState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onFareClick: (FareModel) -> Unit,
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.select_fare_title))
                },
                backgroundColor = AppTheme.colors.material.surface,
            )
        },
        content = {
            ScreenContent(
                status = uiState.status,
                forceLoading = uiState.status.isLoading,
            ) {
                FareList(
                    fares = uiState.fares,
                    onClick = onFareClick
                )
            }
        }
    )
}

@Preview(name = "Fares Content", showBackground = true)
@Composable
fun PreviewRydersContent() {
    AppTheme {
        FareListScreen(
            uiState = FareListState(
                status = ScreenContentStatus.Success,
                fares = fakeFareModels,
            ),
            onFareClick = {}
        )
    }
}
package com.moove.app.feature.home

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.moove.shared.presentation.viewmodel.composableEffect
import com.moove.shared.presentation.viewmodel.composableState
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    navigator: HomeNavigator,
    viewModel: HomeViewModel = koinViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val state by viewModel.composableState()

    HomeScreen(
        uiState = state,
        scaffoldState = scaffoldState,
        onRyderClick = viewModel::onRyderClick,
    )

    viewModel.RenderEffect(navigator = navigator)
}

@Composable
private fun HomeViewModel.RenderEffect(
    navigator: HomeNavigator,
) {
    composableEffect { effect ->
        when (effect) {
            is HomeEffect.GoToRyderList -> {
                navigator.goRyderList()
            }
        }
    }
}

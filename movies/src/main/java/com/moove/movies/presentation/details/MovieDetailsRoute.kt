package com.moove.movies.presentation.details

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.moove.shared.presentation.compose.component.showGenericError
import com.moove.shared.presentation.viewmodel.composableEffect
import com.moove.shared.presentation.viewmodel.composableState

@Composable
fun MovieDetailsRoute(
    navigator: MovieDetailsNavigator,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val state by viewModel.composableState()

    MovieDetailsScreen(
        uiState = state,
        scaffoldState = scaffoldState,
        onBack = viewModel::onBack,
        onRetry = viewModel::onRetry,
    )

    viewModel.RenderEffect(scaffoldState = scaffoldState, navigator = navigator)
}

@Composable
private fun MovieDetailsViewModel.RenderEffect(
    scaffoldState: ScaffoldState,
    navigator: MovieDetailsNavigator,
) {
    val context = LocalContext.current
    composableEffect { effect ->
        when (effect) {
            MovieDetailsEffect.GoBack -> navigator.goBack()
            MovieDetailsEffect.ShowGenericError -> scaffoldState.showGenericError(context)
        }
    }
}

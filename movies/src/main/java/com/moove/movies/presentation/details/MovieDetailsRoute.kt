package com.moove.movies.presentation.details

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.moove.movies.analytics.event.MovieDetailsScreenViewed
import androidx.compose.ui.platform.LocalContext
import com.moove.shared.presentation.compose.component.showGenericError
import com.moove.shared.presentation.viewmodel.composableEffect
import com.moove.shared.presentation.viewmodel.composableState
import io.github.mkhytarmkhoian.herald.compose.TrackScreenView
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailsRoute(
    navigator: MovieDetailsNavigator,
    movieId: Long,
    viewModel: MovieDetailsViewModel = koinViewModel { parametersOf(movieId) },
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val state by viewModel.composableState()

    // Each time the screen resumes, not once per ViewModel: the other way of tracking a screen.
    TrackScreenView(remember(movieId) { MovieDetailsScreenViewed(movieId) })

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

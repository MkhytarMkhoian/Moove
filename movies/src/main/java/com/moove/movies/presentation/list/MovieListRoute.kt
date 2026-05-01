package com.moove.movies.presentation.list

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.collectAsLazyPagingItems
import com.moove.shared.presentation.compose.component.showGenericError
import com.moove.shared.presentation.viewmodel.composableEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieListRoute(
    navigator: MovieListNavigator,
    viewModel: MovieListViewModel = koinViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val lazyMovies = viewModel.movies.collectAsLazyPagingItems()

    MovieListScreen(
        lazyMovies = lazyMovies,
        onMovieClick = viewModel::onMovieClick,
        scaffoldState = scaffoldState,
    )

    viewModel.RenderEffect(scaffoldState = scaffoldState, navigator = navigator)
}

@Composable
private fun MovieListViewModel.RenderEffect(
    scaffoldState: ScaffoldState,
    navigator: MovieListNavigator,
) {
    val context = LocalContext.current
    composableEffect { effect ->
        when (effect) {
            is MovieListEffect.GoToDetails -> navigator.goToDetails(effect.movieId)
            MovieListEffect.ShowGenericError -> scaffoldState.showGenericError(context)
        }
    }
}

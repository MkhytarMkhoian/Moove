package com.moove.movies.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.moove.movies.R
import com.moove.movies.presentation.list.component.MovieListError
import com.moove.movies.presentation.list.component.MovieListItem
import com.moove.movies.presentation.list.model.MovieSummaryModel
import com.moove.movies.presentation.list.model.fakeMovieSummaryModels
import com.moove.shared.presentation.compose.component.ScreenContent
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieListScreen(
    lazyMovies: LazyPagingItems<MovieSummaryModel>,
    onMovieClick: (MovieSummaryModel) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val refresh = lazyMovies.loadState.refresh
    val isEmpty = lazyMovies.itemCount == 0
    val status = when {
        isEmpty && refresh is LoadState.Loading -> ScreenContentStatus.Loading
        isEmpty && refresh is LoadState.Error -> ScreenContentStatus.Failure
        else -> ScreenContentStatus.Success
    }
    val errorMessage = (refresh as? LoadState.Error)?.error?.localizedMessage

    val pullState = rememberPullRefreshState(
        refreshing = refresh is LoadState.Loading && !isEmpty,
        onRefresh = { lazyMovies.refresh() },
    )

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.movies_list_title)) },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            ScreenContent(
                modifier = Modifier.fillMaxSize(),
                status = status,
                error = {
                    MovieListError(
                        onRetry = { lazyMovies.retry() },
                        message = errorMessage,
                    )
                },
            ) {
                Box(modifier = Modifier.pullRefresh(pullState)) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(GRID_SPAN),
                        contentPadding = PaddingValues(4.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(
                            count = lazyMovies.itemCount,
                            key = { index -> lazyMovies[index]?.id ?: index.toLong() },
                        ) { index ->
                            val movie = lazyMovies[index]
                            if (movie != null) {
                                MovieListItem(
                                    movie = movie,
                                    onClick = onMovieClick,
                                    modifier = Modifier.padding(4.dp),
                                )
                            }
                        }

                        appendLoadStateItem(lazyMovies.loadState.append) {
                            lazyMovies.retry()
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = refresh is LoadState.Loading && !isEmpty,
                        state = pullState,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.appendLoadStateItem(
    appendState: LoadState,
    onRetry: () -> Unit,
) {
    when (appendState) {
        is LoadState.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = appendState.error.localizedMessage
                        ?: "",
                    style = MaterialTheme.typography.body2,
                )
                Button(onClick = onRetry, modifier = Modifier.padding(start = 12.dp)) {
                    Text(text = stringResource(id = R.string.movies_retry))
                }
            }
        }

        is LoadState.NotLoading -> Unit
    }
}

private const val GRID_SPAN = 2

@Preview(showBackground = true)
@Composable
private fun PreviewMovieListSuccess() {
    val lazyMovies = remember {
        flowOf(PagingData.from(fakeMovieSummaryModels))
    }.collectAsLazyPagingItems()
    MaterialTheme {
        MovieListScreen(lazyMovies = lazyMovies, onMovieClick = {})
    }
}

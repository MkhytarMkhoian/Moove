package com.moove.movies.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moove.movies.R
import com.moove.movies.presentation.details.component.MovieDetailsContent
import com.moove.movies.presentation.details.model.fakeMovieDetailsModel
import com.moove.shared.presentation.compose.component.ScreenContent
import com.moove.shared.presentation.compose.component.ScreenContentStatus

@Composable
fun MovieDetailsScreen(
    uiState: MovieDetailsState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.details?.title
                            ?: stringResource(id = R.string.movie_details_title),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.movie_details_back_content_description,
                            ),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            ScreenContent(
                modifier = Modifier.fillMaxSize(),
                status = uiState.status,
                error = { MovieDetailsError(onRetry = onRetry) },
            ) {
                uiState.details?.let { MovieDetailsContent(details = it) }
            }
        }
    }
}

@Composable
private fun MovieDetailsError(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.movie_details_error_title),
            style = MaterialTheme.typography.h6,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(id = R.string.movies_retry))
        }
    }
}

@Preview(name = "Movie Details Success", showBackground = true)
@Composable
private fun PreviewMovieDetailsSuccess() {
    MaterialTheme {
        MovieDetailsScreen(
            uiState = MovieDetailsState(
                status = ScreenContentStatus.Success,
                details = fakeMovieDetailsModel,
            ),
            onBack = {},
            onRetry = {},
        )
    }
}

@Preview(name = "Movie Details Failure", showBackground = true)
@Composable
private fun PreviewMovieDetailsFailure() {
    MaterialTheme {
        MovieDetailsScreen(
            uiState = MovieDetailsState(status = ScreenContentStatus.Failure),
            onBack = {},
            onRetry = {},
        )
    }
}

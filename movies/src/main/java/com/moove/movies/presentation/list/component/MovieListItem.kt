package com.moove.movies.presentation.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.moove.movies.R
import com.moove.movies.presentation.list.model.MovieSummaryModel

@Composable
internal fun MovieListItem(
    movie: MovieSummaryModel,
    onClick: (MovieSummaryModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(movie) },
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl())
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(
                    id = R.string.movie_poster_content_description,
                    movie.title,
                ),
                placeholder = painterResource(id = R.drawable.ic_poster_placeholder),
                error = painterResource(id = R.drawable.ic_poster_placeholder),
                fallback = painterResource(id = R.drawable.ic_poster_placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(POSTER_ASPECT_RATIO),
            )
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(id = R.string.movies_rating_format, movie.rating),
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = movie.releaseYear ?: stringResource(id = R.string.movies_release_year_unknown),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

private const val POSTER_ASPECT_RATIO = 2f / 3f

@Preview(showBackground = true, widthDp = 180)
@Composable
private fun PreviewMovieListItem() {
    MaterialTheme {
        MovieListItem(
            movie = MovieSummaryModel(
                id = 1L,
                title = "The Matrix Resurrections",
                posterPath = null,
                rating = 8.2f,
                releaseYear = "1999",
            ),
            onClick = {},
            modifier = Modifier.padding(8.dp),
        )
    }
}

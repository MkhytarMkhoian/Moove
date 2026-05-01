package com.moove.movies.presentation.details.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.moove.movies.R
import com.moove.movies.presentation.details.model.MovieDetailsModel

@Composable
internal fun MovieDetailsContent(
    details: MovieDetailsModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        AsyncImage(
            model = details.backdropUrl() ?: details.posterUrl(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
        )

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = details.title,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFFFFC107),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "%.1f".format(details.rating),
                    style = MaterialTheme.typography.body2,
                )
                if (!details.releaseDate.isNullOrBlank()) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = details.releaseDate,
                        style = MaterialTheme.typography.body2,
                    )
                }
                val runtime = details.runtimeMinutes
                if (runtime != null) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(id = R.string.movie_details_runtime_minutes_format, runtime),
                        style = MaterialTheme.typography.body2,
                    )
                }
            }

            if (details.genres.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                GenresRow(genres = details.genres)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.movie_details_overview_title),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = details.overview.ifBlank { "—" },
                style = MaterialTheme.typography.body1,
            )
        }
    }
}

@Composable
private fun GenresRow(genres: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        genres.take(MAX_GENRES).forEach { genre ->
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colors.primary.copy(alpha = 0.12f),
            ) {
                Text(
                    text = genre,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

private const val MAX_GENRES = 4

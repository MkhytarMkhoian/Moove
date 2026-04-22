package com.moove.movies.presentation.list.model

import android.os.Parcelable
import com.moove.movies.BuildConfig
import com.moove.movies.domain.model.MovieSummary
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieSummaryModel(
    val id: Long,
    val title: String,
    val posterPath: String?,
    val rating: Float,
    val releaseYear: String?,
) : Parcelable {

    fun posterUrl(size: String = DEFAULT_POSTER_SIZE): String? =
        posterPath?.let { BuildConfig.TMDB_IMAGE_BASE_URL + size + it }

    private companion object {
        const val DEFAULT_POSTER_SIZE = "w342"
    }
}

fun MovieSummary.asPresentation(): MovieSummaryModel = MovieSummaryModel(
    id = id,
    title = title,
    posterPath = posterPath,
    rating = rating,
    releaseYear = releaseDate?.year?.toString(),
)

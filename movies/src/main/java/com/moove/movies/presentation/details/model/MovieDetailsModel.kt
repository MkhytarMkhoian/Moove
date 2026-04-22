package com.moove.movies.presentation.details.model

import android.os.Parcelable
import com.moove.movies.BuildConfig
import com.moove.movies.domain.model.MovieDetails
import kotlinx.parcelize.Parcelize
import java.time.format.DateTimeFormatter

@Parcelize
data class MovieDetailsModel(
    val id: Long,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String,
    val releaseDate: String?,
    val rating: Float,
    val runtimeMinutes: Int?,
    val genres: List<String>,
) : Parcelable {

    fun posterUrl(size: String = DEFAULT_POSTER_SIZE): String? =
        posterPath?.let { BuildConfig.TMDB_IMAGE_BASE_URL + size + it }

    fun backdropUrl(size: String = DEFAULT_BACKDROP_SIZE): String? =
        backdropPath?.let { BuildConfig.TMDB_IMAGE_BASE_URL + size + it }

    private companion object {
        const val DEFAULT_POSTER_SIZE = "w500"
        const val DEFAULT_BACKDROP_SIZE = "w780"
    }
}

fun MovieDetails.asPresentation(): MovieDetailsModel {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return MovieDetailsModel(
        id = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        releaseDate = releaseDate?.format(formatter),
        rating = rating,
        runtimeMinutes = runtimeMinutes,
        genres = genres.map { it.name },
    )
}

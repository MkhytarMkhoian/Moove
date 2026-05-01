package com.moove.movies.data.net.dto

import com.moove.movies.domain.model.MovieDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsDTO(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "overview") val overview: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "runtime") val runtimeMinutes: Int?,
    @Json(name = "genres") val genres: List<GenreDTO>?,
)

fun MovieDetailsDTO.asDomain(): MovieDetails = MovieDetails(
    id = id,
    title = title,
    posterPath = posterPath,
    backdropPath = backdropPath,
    overview = overview.orEmpty(),
    releaseDate = parseReleaseDate(releaseDate),
    rating = voteAverage.toFloat(),
    runtimeMinutes = runtimeMinutes?.takeIf { it > 0 },
    genres = genres.orEmpty().asDomain(),
)

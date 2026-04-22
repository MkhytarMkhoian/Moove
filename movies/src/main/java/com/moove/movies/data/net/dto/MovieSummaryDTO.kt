package com.moove.movies.data.net.dto

import com.moove.movies.domain.model.MovieSummary
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.format.DateTimeParseException

@JsonClass(generateAdapter = true)
data class MovieSummaryDTO(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "release_date") val releaseDate: String?,
)

fun MovieSummaryDTO.asDomain(): MovieSummary = MovieSummary(
    id = id,
    title = title,
    posterPath = posterPath,
    rating = voteAverage.toFloat(),
    releaseDate = parseReleaseDate(releaseDate),
)

fun List<MovieSummaryDTO>.asDomain(): List<MovieSummary> = map { it.asDomain() }

internal fun parseReleaseDate(raw: String?): LocalDate? =
    raw?.takeIf(String::isNotBlank)?.let {
        try {
            LocalDate.parse(it)
        } catch (_: DateTimeParseException) {
            null
        }
    }

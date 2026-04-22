package com.moove.movies.domain.model

import java.time.LocalDate

data class MovieDetails(
    val id: Long,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String,
    val releaseDate: LocalDate?,
    val rating: Float,
    val runtimeMinutes: Int?,
    val genres: List<Genre>,
)

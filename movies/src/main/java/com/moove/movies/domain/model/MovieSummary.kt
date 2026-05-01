package com.moove.movies.domain.model

import java.time.LocalDate

data class MovieSummary(
    val id: Long,
    val title: String,
    val posterPath: String?,
    val rating: Float,
    val releaseDate: LocalDate?,
)

package com.moove.movies.domain.exceptions

class MoviesApiException(
    val statusCode: Int,
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message ?: "TMDB API error ($statusCode)", cause)

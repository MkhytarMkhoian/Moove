package com.moove.movies.domain.exceptions

class MovieNotFoundException(
    val movieId: Long,
    cause: Throwable? = null,
) : Exception("Movie $movieId not found", cause)

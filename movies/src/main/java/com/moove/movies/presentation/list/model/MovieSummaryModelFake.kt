package com.moove.movies.presentation.list.model

internal val fakeMovieSummaryModels: List<MovieSummaryModel>
    get() = listOf(
        MovieSummaryModel(
            id = 1L,
            title = "The Matrix",
            posterPath = null,
            rating = 8.2f,
            releaseYear = "1999",
        ),
        MovieSummaryModel(
            id = 2L,
            title = "Inception",
            posterPath = null,
            rating = 8.4f,
            releaseYear = "2010",
        ),
        MovieSummaryModel(
            id = 3L,
            title = "Interstellar",
            posterPath = null,
            rating = 8.6f,
            releaseYear = "2014",
        ),
    )

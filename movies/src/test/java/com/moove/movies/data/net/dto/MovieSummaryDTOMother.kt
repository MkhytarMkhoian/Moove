package com.moove.movies.data.net.dto

import com.github.javafaker.Faker

private val faker = Faker()

fun randomMovieSummaryDTO(
    id: Long = faker.number().randomNumber(),
    title: String = faker.book().title(),
    posterPath: String? = "/${faker.lorem().characters(8)}.jpg",
    voteAverage: Double = faker.number().randomDouble(1, 1, 10),
    releaseDate: String? = "1999-03-31",
): MovieSummaryDTO = MovieSummaryDTO(
    id = id,
    title = title,
    posterPath = posterPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
)

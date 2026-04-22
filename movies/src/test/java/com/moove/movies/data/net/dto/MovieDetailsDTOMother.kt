package com.moove.movies.data.net.dto

import com.github.javafaker.Faker

private val faker = Faker()

fun randomGenreDTO(
    id: Long = faker.number().randomNumber(),
    name: String = faker.book().genre(),
): GenreDTO = GenreDTO(id = id, name = name)

fun randomMovieDetailsDTO(
    id: Long = faker.number().randomNumber(),
    title: String = faker.book().title(),
    posterPath: String? = "/${faker.lorem().characters(8)}.jpg",
    backdropPath: String? = "/${faker.lorem().characters(8)}.jpg",
    overview: String? = faker.lorem().paragraph(),
    releaseDate: String? = "1999-03-31",
    voteAverage: Double = faker.number().randomDouble(1, 1, 10),
    runtimeMinutes: Int? = faker.number().numberBetween(60, 240),
    genres: List<GenreDTO>? = listOf(randomGenreDTO(), randomGenreDTO()),
): MovieDetailsDTO = MovieDetailsDTO(
    id = id,
    title = title,
    posterPath = posterPath,
    backdropPath = backdropPath,
    overview = overview,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    runtimeMinutes = runtimeMinutes,
    genres = genres,
)

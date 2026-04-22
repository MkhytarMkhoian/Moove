package com.moove.movies.data.net.dto

import com.moove.movies.domain.model.Genre
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreDTO(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
)

fun GenreDTO.asDomain(): Genre = Genre(id = id, name = name)

fun List<GenreDTO>.asDomain(): List<Genre> = map { it.asDomain() }

package com.moove.tickets.data.local.dto

import com.moove.tickets.domain.model.Fare
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FareDTO(
    @Json(name = "description")
    val description: String,
    @Json(name = "price")
    val price: Float,
)

fun FareDTO.asDomain() = Fare(
    description = description,
    price = price,
)

fun List<FareDTO>.asDomain() = map { it.asDomain() }

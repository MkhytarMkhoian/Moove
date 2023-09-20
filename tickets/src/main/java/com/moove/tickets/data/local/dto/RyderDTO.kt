package com.moove.tickets.data.local.dto

import com.moove.tickets.domain.model.Ryder
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RyderDTO(
    @Json(name = "fares")
    val fares: List<FareDTO>,
    @Json(name = "subtext")
    val subtext: String?,
)

// TODO add extra tests
fun RyderDTO.asDomain(id: String) = Ryder(
    id = id,
    fares = fares.asDomain(),
    subtext = subtext,
)
package com.moove.tickets.data.local.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TicketsDataDTO(
    @Json(name = "Adult")
    val adult: RyderDTO,
    @Json(name = "Child")
    val child: RyderDTO,
    @Json(name = "Senior")
    val senior: RyderDTO,
)
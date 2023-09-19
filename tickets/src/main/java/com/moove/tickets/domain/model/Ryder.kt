package com.moove.tickets.domain.model

data class Ryder(
    val id: String,
    val fares: List<Fare>,
    val subtext: String?,
)
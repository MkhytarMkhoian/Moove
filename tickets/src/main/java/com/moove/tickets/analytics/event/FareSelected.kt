package com.moove.tickets.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

data class FareSelected(
    val ryderId: String,
    val description: String,
    val price: Double,
) : Event {
    override val name = "fare_selected"
    override val parameters = parameters {
        put("ryder_id", ryderId)
        put("fare", description)
        put("price", price)
    }
}

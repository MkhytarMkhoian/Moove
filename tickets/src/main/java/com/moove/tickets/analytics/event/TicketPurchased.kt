package com.moove.tickets.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

/**
 * The purchase.
 *
 * Six facts describe a purchase, so they travel as one [Params] value rather than six
 * constructor arguments: the call site names them once, and a tracker that needs the whole
 * purchase passes one thing on.
 */
data class TicketPurchased(val params: Params) : Event {

    data class Params(
        val ryderId: String,
        val fare: String,
        val count: Int,
        val revenue: Double,
        val currency: String,
        val transactionId: String,
    )

    override val name = NAME
    override val parameters = parameters {
        put("ryder_id", params.ryderId)
        put("fare", params.fare)
        put("count", params.count)
        put("total", params.revenue)
    }

    companion object {
        const val NAME = "ticket_purchased"
    }
}

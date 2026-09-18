package com.moove.tickets.analytics.event

import io.github.mkhytarmkhoian.herald.adjust.RevenueEvent
import io.github.mkhytarmkhoian.herald.parameters

/**
 * The purchase. A [RevenueEvent], so Adjust sends it under the purchase token with the revenue
 * attached and deduplicates on the transaction id; Firebase and Mixpanel get it through the
 * feature's factories.
 *
 * Six facts describe a purchase, so they travel as one [Params] value rather than six
 * constructor arguments: the call site names them once, and a tracker that needs the whole
 * purchase passes one thing on.
 */
data class TicketPurchased(val params: Params) : RevenueEvent {

    data class Params(
        val ryderId: String,
        val fare: String,
        val count: Int,
        val revenue: Double,
        val currency: String,
        val deduplicationId: String,
    )

    override val name = NAME
    override val revenue get() = params.revenue
    override val currency get() = params.currency
    override val deduplicationId get() = params.deduplicationId
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

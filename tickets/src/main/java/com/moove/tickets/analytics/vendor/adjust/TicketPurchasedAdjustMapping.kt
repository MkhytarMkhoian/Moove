package com.moove.tickets.analytics.vendor.adjust

import com.moove.tickets.analytics.event.TicketPurchased
import io.github.mkhytarmkhoian.herald.adjust.AdjustRevenueEvent

/** The purchase as Adjust revenue, deduplicated by the transaction id. */
internal fun TicketPurchased.toAdjustRevenueEvent() = AdjustRevenueEvent(
    name = name,
    revenue = params.revenue,
    currency = params.currency,
    deduplicationId = params.transactionId,
    parameters = parameters,
)

package com.moove.tickets.analytics.vendor.amplitude

import com.moove.tickets.analytics.event.TicketPurchased
import io.github.mkhytarmkhoian.herald.amplitude.AmplitudeRevenueEvent

/** The purchase through Amplitude's revenue API: a unit price, a quantity, and the transaction id. */
internal fun TicketPurchased.toAmplitudeRevenueEvent() = AmplitudeRevenueEvent(
    name = name,
    price = params.revenue / params.count,
    quantity = params.count,
    revenue = params.revenue,
    currency = params.currency,
    productId = params.fare,
    insertId = params.transactionId,
    parameters = parameters,
)

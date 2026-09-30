package com.moove.tickets.analytics.vendor.appsflyer

import com.moove.tickets.analytics.event.TicketPurchased
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerPurchaseEvent

/** The purchase as AppsFlyer's `af_purchase`, with the revenue under `af_revenue`. */
internal fun TicketPurchased.toAppsFlyerPurchaseEvent() = AppsFlyerPurchaseEvent(
    name = name,
    revenue = params.revenue,
    currency = params.currency,
    contentId = params.fare,
    quantity = params.count,
    orderId = params.transactionId,
    parameters = parameters,
)

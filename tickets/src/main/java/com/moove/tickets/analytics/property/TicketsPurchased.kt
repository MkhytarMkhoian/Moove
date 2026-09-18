package com.moove.tickets.analytics.property

import io.github.mkhytarmkhoian.herald.AnalyticsValue
import io.github.mkhytarmkhoian.herald.UserProperty

/** Tickets in the most recent purchase. Numeric on purpose: Mixpanel can filter on `> 2`. */
data class TicketsPurchased(val count: Int) : UserProperty {
    override val name = "tickets_purchased"
    override val value = AnalyticsValue.Int(count)
}

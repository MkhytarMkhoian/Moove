package com.moove.tickets.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

/** Every tap on + or −. Noise for the paid vendors, so Adjust and Mixpanel drop it. */
data class TicketCountChanged(val count: Int) : Event {
    override val name = "ticket_count_changed"
    override val parameters = parameters { put("count", count) }
}

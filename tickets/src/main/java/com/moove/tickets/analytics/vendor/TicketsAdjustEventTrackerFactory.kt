package com.moove.tickets.analytics.vendor

import com.adjust.sdk.AdjustInstance
import com.moove.tickets.analytics.event.TicketCountChanged
import com.moove.tickets.analytics.event.TicketPurchased
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTracker
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.TokenAdjustEventTrackerFactory

/** The dashboard token for the purchase event. A placeholder here; a real app reads it from config. */
private const val TICKET_PURCHASED_TOKEN = "abc123"

/**
 * What reaches Adjust from the tickets feature: the purchase, under its token, with revenue
 * attached by Herald's `toAdjustEvent` because the event is a `RevenueEvent`. The count taps are
 * claimed and dropped — explicitly, so nobody later adds a token for them by accident. The rest
 * is declined and, Adjust's chain having no generic terminator, never sent.
 */
class TicketsAdjustEventTrackerFactory(adjust: AdjustInstance) : AdjustEventTrackerFactory {

    private val tokenFactory = TokenAdjustEventTrackerFactory(
        tokens = mapOf(TicketPurchased.NAME to TICKET_PURCHASED_TOKEN),
        adjust = adjust,
    )

    override fun create(event: Event): Resolution<AdjustEventTracker> = when (event) {
        is TicketCountChanged -> Resolution.Dropped
        else -> tokenFactory.create(event)
    }
}

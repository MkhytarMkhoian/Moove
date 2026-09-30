package com.moove.tickets.analytics.vendor

import com.adjust.sdk.AdjustInstance
import com.moove.tickets.analytics.event.TicketCountChanged
import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.analytics.vendor.adjust.toAdjustRevenueEvent
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTracker
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.trackers.RevenueEventTracker

/** The dashboard token for the purchase event. A placeholder here; a real app reads it from config. */
private const val TICKET_PURCHASED_TOKEN = "abc123"

/**
 * The purchase goes to Adjust as revenue under its token. Count taps are dropped explicitly, so
 * nobody later adds a token for them by accident; the rest is declined and never sent.
 */
class TicketsAdjustEventTrackerFactory(
    private val adjust: AdjustInstance,
) : AdjustEventTrackerFactory {

    override fun create(event: Event): Resolution<AdjustEventTracker> = when (event) {
        is TicketPurchased -> Resolution.Claimed(
            RevenueEventTracker(event.toAdjustRevenueEvent(), TICKET_PURCHASED_TOKEN, adjust),
        )
        is TicketCountChanged -> Resolution.Dropped
        else -> Resolution.Declined
    }
}

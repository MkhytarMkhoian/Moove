package com.moove.tickets.analytics.vendor.mixpanel

import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.moove.tickets.analytics.event.TicketPurchased
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelEventTracker

/**
 * Mixpanel keeps revenue on the people profile through `trackCharge`, an API the generic event
 * path cannot reach. This is the second handler for a purchase; the first is Herald's generic one.
 */
class ChargeMixpanelEventTracker(
    private val event: TicketPurchased,
    private val mixpanel: MixpanelAPI,
) : MixpanelEventTracker {

    override suspend fun track() {
        mixpanel.people.trackCharge(event.revenue, null)
    }
}

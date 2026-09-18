package com.moove.tickets.analytics.vendor

import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.moove.tickets.analytics.event.TicketCountChanged
import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.analytics.vendor.mixpanel.ChargeMixpanelEventTracker
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelEventTracker
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelEventTrackerFactory
import io.github.mkhytarmkhoian.herald.mixpanel.trackers.GenericEventTracker

/**
 * The purchase goes out twice: as the event, through Herald's own generic tracker, and as a
 * charge on the profile, through the feature's. Count taps are dropped, as for Adjust.
 */
class TicketsMixpanelEventTrackerFactory(
    private val mixpanel: MixpanelAPI,
) : MixpanelEventTrackerFactory {

    override fun create(event: Event): Resolution<MixpanelEventTracker> = when (event) {
        is TicketCountChanged -> Resolution.Dropped
        is TicketPurchased -> Resolution.Claimed(
            GenericEventTracker(event, mixpanel),
            ChargeMixpanelEventTracker(event, mixpanel),
        )
        else -> Resolution.Declined
    }
}

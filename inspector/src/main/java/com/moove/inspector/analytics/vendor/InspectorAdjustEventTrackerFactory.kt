package com.moove.inspector.analytics.vendor

import com.adjust.sdk.AdjustInstance
import com.moove.inspector.analytics.event.AdImpressionSimulated
import com.moove.inspector.analytics.vendor.adjust.toAdjustAdRevenueEvent
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTracker
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.trackers.AdRevenueEventTracker

/** The simulated ad impression, through Adjust's ad-revenue API. */
class InspectorAdjustEventTrackerFactory(
    private val adjust: AdjustInstance,
) : AdjustEventTrackerFactory {

    override fun create(event: Event): Resolution<AdjustEventTracker> = when (event) {
        is AdImpressionSimulated -> Resolution.Claimed(AdRevenueEventTracker(event.toAdjustAdRevenueEvent(), adjust))
        else -> Resolution.Declined
    }
}

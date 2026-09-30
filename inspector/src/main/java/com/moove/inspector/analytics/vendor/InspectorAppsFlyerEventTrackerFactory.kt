package com.moove.inspector.analytics.vendor

import com.appsflyer.AppsFlyerLib
import com.moove.inspector.analytics.event.AdImpressionSimulated
import com.moove.inspector.analytics.vendor.appsflyer.toAppsFlyerAdRevenueEvent
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTracker
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTrackerFactory
import io.github.mkhytarmkhoian.herald.appsflyer.trackers.AdRevenueEventTracker

/** The simulated ad impression, through AppsFlyer's ad-revenue API. */
class InspectorAppsFlyerEventTrackerFactory(
    private val appsFlyer: AppsFlyerLib,
) : AppsFlyerEventTrackerFactory {

    override fun create(event: Event): Resolution<AppsFlyerEventTracker> = when (event) {
        is AdImpressionSimulated -> Resolution.Claimed(AdRevenueEventTracker(event.toAppsFlyerAdRevenueEvent(), appsFlyer))
        else -> Resolution.Declined
    }
}

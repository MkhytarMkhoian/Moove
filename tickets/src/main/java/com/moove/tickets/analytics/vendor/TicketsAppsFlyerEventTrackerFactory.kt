package com.moove.tickets.analytics.vendor

import android.content.Context
import com.appsflyer.AppsFlyerLib
import com.moove.tickets.analytics.event.FareSelected
import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.analytics.vendor.appsflyer.InitiatedCheckoutAppsFlyerEventTracker
import com.moove.tickets.analytics.vendor.appsflyer.toAppsFlyerPurchaseEvent
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTracker
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTrackerFactory
import io.github.mkhytarmkhoian.herald.appsflyer.trackers.PurchaseEventTracker

/**
 * The purchase as `af_purchase` and choosing a fare as `af_initiated_checkout`. Anything declined
 * here doesn't reach AppsFlyer.
 */
class TicketsAppsFlyerEventTrackerFactory(
    private val appsFlyer: AppsFlyerLib,
    private val context: Context,
) : AppsFlyerEventTrackerFactory {

    override fun create(event: Event): Resolution<AppsFlyerEventTracker> = when (event) {
        is TicketPurchased -> Resolution.Claimed(
            PurchaseEventTracker(event.toAppsFlyerPurchaseEvent(), appsFlyer, context),
        )
        is FareSelected -> Resolution.Claimed(InitiatedCheckoutAppsFlyerEventTracker(event, appsFlyer, context))
        else -> Resolution.Declined
    }
}

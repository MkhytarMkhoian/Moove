package com.moove.tickets.analytics.vendor.appsflyer

import android.content.Context
import com.appsflyer.AppsFlyerLib
import com.appsflyer.share.AFInAppEventParameterName
import com.appsflyer.share.AFInAppEventType
import com.moove.tickets.analytics.event.FareSelected
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTracker

/** Choosing a fare is where checkout starts: AppsFlyer's predefined `af_initiated_checkout`. */
class InitiatedCheckoutAppsFlyerEventTracker(
    private val event: FareSelected,
    private val appsFlyer: AppsFlyerLib,
    private val context: Context,
) : AppsFlyerEventTracker {

    override suspend fun track() {
        appsFlyer.logEvent(
            context,
            AFInAppEventType.INITIATED_CHECKOUT,
            mapOf(
                AFInAppEventParameterName.CONTENT_ID to event.description,
                AFInAppEventParameterName.PRICE to event.price,
            ),
        )
    }
}

package com.moove.analytics.vendor.appsflyer

import android.content.Context
import com.appsflyer.AppsFlyerLib
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTracker
import io.github.mkhytarmkhoian.herald.appsflyer.toAppsFlyerEventValues

/** Logs one event to AppsFlyer under [eventName] instead of its own name, parameters as they are. */
class MappedAppsFlyerEventTracker(
    private val event: Event,
    private val eventName: String,
    private val appsFlyer: AppsFlyerLib,
    private val context: Context,
) : AppsFlyerEventTracker {

    override suspend fun track() {
        appsFlyer.logEvent(context, eventName, event.parameters.toAppsFlyerEventValues())
    }
}

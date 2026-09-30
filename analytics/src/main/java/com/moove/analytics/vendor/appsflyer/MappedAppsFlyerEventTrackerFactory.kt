package com.moove.analytics.vendor.appsflyer

import android.content.Context
import com.appsflyer.AppsFlyerLib
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTracker
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTrackerFactory
import kotlin.reflect.KClass

/**
 * Sends each event whose class is in [eventNames] to AppsFlyer under the mapped name, parameters
 * unchanged. For events that only need a new name; one that needs AppsFlyer's parameter keys gets a
 * tracker of its own.
 *
 * Matches the event's exact class, so a subtype is not covered by its parent's entry.
 */
class MappedAppsFlyerEventTrackerFactory(
    private val eventNames: Map<KClass<out Event>, String>,
    private val appsFlyer: AppsFlyerLib,
    private val context: Context,
) : AppsFlyerEventTrackerFactory {

    override fun create(event: Event): Resolution<AppsFlyerEventTracker> =
        eventNames[event::class]
            ?.let { eventName -> Resolution.Claimed(MappedAppsFlyerEventTracker(event, eventName, appsFlyer, context)) }
            ?: Resolution.Declined
}

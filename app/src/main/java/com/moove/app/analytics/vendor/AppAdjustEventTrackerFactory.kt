package com.moove.app.analytics.vendor

import com.adjust.sdk.AdjustInstance
import com.moove.app.analytics.event.DeepLinkOpened
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTracker
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.trackers.TokenEventTracker

/** The dashboard token for a re-engagement deep link. A placeholder; a real app reads it from config. */
private const val DEEP_LINK_OPENED_TOKEN = "dl0001"

/**
 * The app module writes no handler of its own: routing is the only decision here, and Herald's
 * [TokenEventTracker] already builds the Adjust payload with the event's parameters as callback
 * parameters. A deep link is attribution data, which is what Adjust is for; nothing else the app
 * module reports is.
 */
class AppAdjustEventTrackerFactory(
    private val adjust: AdjustInstance,
) : AdjustEventTrackerFactory {

    override fun create(event: Event): Resolution<AdjustEventTracker> = when (event) {
        is DeepLinkOpened -> Resolution.Claimed(TokenEventTracker(event, DEEP_LINK_OPENED_TOKEN, adjust))
        else -> Resolution.Declined
    }
}

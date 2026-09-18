package com.moove.tickets.analytics.vendor

import com.google.firebase.analytics.FirebaseAnalytics
import com.moove.tickets.analytics.event.ContentEvent
import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.analytics.vendor.firebase.PurchaseFirebaseEventTracker
import com.moove.tickets.analytics.vendor.firebase.SelectContentFirebaseEventTracker
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.firebase.FirebaseEventTracker
import io.github.mkhytarmkhoian.herald.firebase.FirebaseEventTrackerFactory

/**
 * Routing only: the two events Firebase must see differently get their own trackers; everything
 * else is declined and reaches Firebase under its own name through the generic terminator.
 */
class TicketsFirebaseEventTrackerFactory(
    private val analytics: FirebaseAnalytics,
) : FirebaseEventTrackerFactory {

    override fun create(event: Event): Resolution<FirebaseEventTracker> = when (event) {
        is TicketPurchased -> Resolution.Claimed(PurchaseFirebaseEventTracker(event, analytics))
        is ContentEvent -> Resolution.Claimed(SelectContentFirebaseEventTracker(event, analytics))
        else -> Resolution.Declined
    }
}

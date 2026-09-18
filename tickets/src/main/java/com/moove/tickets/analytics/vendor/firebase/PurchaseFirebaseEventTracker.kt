package com.moove.tickets.analytics.vendor.firebase

import com.google.firebase.analytics.FirebaseAnalytics
import com.moove.tickets.analytics.event.TicketPurchased
import io.github.mkhytarmkhoian.herald.firebase.FirebaseEventTracker
import io.github.mkhytarmkhoian.herald.firebase.toBundle

/**
 * A purchase is not "an event under its own name" to Firebase: GA4 reports revenue only from its
 * reserved `purchase` event with the reserved parameter names, so the tracker translates.
 */
class PurchaseFirebaseEventTracker(
    private val event: TicketPurchased,
    private val analytics: FirebaseAnalytics,
) : FirebaseEventTracker {

    override suspend fun track() {
        val bundle = event.parameters.toBundle().apply {
            putDouble(FirebaseAnalytics.Param.VALUE, event.revenue)
            putString(FirebaseAnalytics.Param.CURRENCY, event.currency)
            putString(FirebaseAnalytics.Param.TRANSACTION_ID, event.deduplicationId)
            putLong(FirebaseAnalytics.Param.QUANTITY, event.params.count.toLong())
        }
        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
    }
}

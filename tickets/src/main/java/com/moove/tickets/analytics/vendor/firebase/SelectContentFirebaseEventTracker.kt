package com.moove.tickets.analytics.vendor.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.moove.tickets.analytics.event.ContentEvent
import io.github.mkhytarmkhoian.herald.firebase.FirebaseEventTracker

/** Reports a [ContentEvent] as GA4's reserved `select_content`, so it lands in the standard report. */
class SelectContentFirebaseEventTracker(
    private val event: ContentEvent,
    private val analytics: FirebaseAnalytics,
) : FirebaseEventTracker {

    override suspend fun track() {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, event.contentType)
            putString(FirebaseAnalytics.Param.ITEM_ID, event.itemId)
        }
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}

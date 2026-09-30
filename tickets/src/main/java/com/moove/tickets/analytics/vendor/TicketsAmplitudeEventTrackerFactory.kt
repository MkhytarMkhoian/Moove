package com.moove.tickets.analytics.vendor

import com.amplitude.android.Amplitude
import com.moove.tickets.analytics.event.TicketCountChanged
import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.analytics.vendor.amplitude.toAmplitudeRevenueEvent
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.amplitude.AmplitudeEventTracker
import io.github.mkhytarmkhoian.herald.amplitude.AmplitudeEventTrackerFactory
import io.github.mkhytarmkhoian.herald.amplitude.trackers.RevenueEventTracker

/**
 * The purchase goes through Amplitude's revenue API. Count taps are dropped; everything else goes
 * out generically.
 */
class TicketsAmplitudeEventTrackerFactory(
    private val amplitude: Amplitude,
) : AmplitudeEventTrackerFactory {

    override fun create(event: Event): Resolution<AmplitudeEventTracker> = when (event) {
        is TicketPurchased -> Resolution.Claimed(RevenueEventTracker(event.toAmplitudeRevenueEvent(), amplitude))
        is TicketCountChanged -> Resolution.Dropped
        else -> Resolution.Declined
    }
}

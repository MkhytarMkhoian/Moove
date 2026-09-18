package com.moove.tickets.analytics.vendor.mixpanel

import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.moove.tickets.analytics.event.TicketPurchased
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ChargeMixpanelEventTrackerTest {

    private val mixpanel: MixpanelAPI = mockk(relaxed = true)
    private val purchase = TicketPurchased(
        TicketPurchased.Params(
            ryderId = "r1", fare = "Adult", count = 2, revenue = 9.98, currency = "USD", deduplicationId = "txn-1",
        )
    )

    @Test
    fun `given a purchase when tracking then the revenue is charged to the profile and nothing is tracked as an event`() = runTest {
        ChargeMixpanelEventTracker(purchase, mixpanel).track()

        val people = mixpanel.people
        verify { people.trackCharge(9.98, null) }
        verify(exactly = 0) { mixpanel.trackMap(any(), any()) }
    }
}

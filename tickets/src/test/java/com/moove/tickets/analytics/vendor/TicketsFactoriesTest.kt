package com.moove.tickets.analytics.vendor

import com.adjust.sdk.AdjustInstance
import com.google.firebase.analytics.FirebaseAnalytics
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.moove.tickets.analytics.event.RyderSelected
import com.moove.tickets.analytics.event.TicketCountChanged
import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.analytics.property.SeatPreference
import com.moove.tickets.analytics.property.TicketsPurchased
import com.moove.tickets.analytics.vendor.firebase.PurchaseFirebaseEventTracker
import com.moove.tickets.analytics.vendor.firebase.SelectContentFirebaseEventTracker
import com.moove.tickets.analytics.vendor.mixpanel.ChargeMixpanelEventTracker
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.setters.GenericPropertySetter
import io.github.mkhytarmkhoian.herald.adjust.trackers.TokenEventTracker
import io.github.mkhytarmkhoian.herald.mixpanel.trackers.GenericEventTracker
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** The routing each feature factory does. What the handlers send is each handler's own test. */
class TicketsFactoriesTest {

    private val adjust: AdjustInstance = mockk(relaxed = true)
    private val mixpanel: MixpanelAPI = mockk(relaxed = true)
    private val firebase: FirebaseAnalytics = mockk(relaxed = true)

    private val purchase = TicketPurchased(
        TicketPurchased.Params(
            ryderId = "r1", fare = "Adult", count = 2, revenue = 9.98, currency = "USD", deduplicationId = "txn-1",
        )
    )
    private val other = object : Event { override val name = "anything_else" }

    @Test
    fun `given the purchase when routing for Adjust then it goes out under its token, count taps are dropped, the rest is declined`() {
        val factory = TicketsAdjustEventTrackerFactory(adjust)

        assertIs<TokenEventTracker>(factory.create(purchase).handlers.single())
        assertEquals(Resolution.Dropped, factory.create(TicketCountChanged(2)))
        assertEquals(Resolution.Declined, factory.create(other))
    }

    @Test
    fun `given a property when routing for Adjust then only the named one is claimed, with Herald's setter`() {
        val factory = TicketsAdjustPropertySetterFactory(adjust)

        assertIs<GenericPropertySetter>(factory.create(TicketsPurchased(2)).handlers.single())
        assertEquals(Resolution.Declined, factory.create(SeatPreference("window")))
    }

    @Test
    fun `given the purchase when routing for Mixpanel then Herald's tracker and the charge both run, count taps dropped`() {
        val factory = TicketsMixpanelEventTrackerFactory(mixpanel)

        val handlers = factory.create(purchase).handlers
        assertEquals(2, handlers.size)
        assertIs<GenericEventTracker>(handlers[0])
        assertIs<ChargeMixpanelEventTracker>(handlers[1])
        assertEquals(Resolution.Dropped, factory.create(TicketCountChanged(1)))
        assertEquals(Resolution.Declined, factory.create(other))
    }

    @Test
    fun `given the purchase or a content event when routing for Firebase then each gets its tracker`() {
        val factory = TicketsFirebaseEventTrackerFactory(firebase)

        assertIs<PurchaseFirebaseEventTracker>(factory.create(purchase).handlers.single())
        assertIs<SelectContentFirebaseEventTracker>(factory.create(RyderSelected("r1")).handlers.single())
        assertEquals(Resolution.Declined, factory.create(other))
    }
}

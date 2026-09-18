package com.moove.tickets.analytics.vendor.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.moove.tickets.analytics.event.TicketPurchased
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class PurchaseFirebaseEventTrackerTest {

    private val firebase: FirebaseAnalytics = mockk(relaxed = true)
    private val purchase = TicketPurchased(
        TicketPurchased.Params(
            ryderId = "r1", fare = "Adult", count = 2, revenue = 9.98, currency = "USD", deduplicationId = "txn-1",
        )
    )

    @Before
    fun stubBundle() {
        mockkConstructor(Bundle::class)
        every { anyConstructed<Bundle>().putString(any(), any()) } returns Unit
        every { anyConstructed<Bundle>().putLong(any(), any()) } returns Unit
        every { anyConstructed<Bundle>().putDouble(any(), any()) } returns Unit
    }

    @After
    fun unstubBundle() = unmockkConstructor(Bundle::class)

    @Test
    fun `given a purchase when tracking then GA4's purchase event carries the reserved parameters`() = runTest {
        PurchaseFirebaseEventTracker(purchase, firebase).track()

        verify { anyConstructed<Bundle>().putDouble(FirebaseAnalytics.Param.VALUE, 9.98) }
        verify { anyConstructed<Bundle>().putString(FirebaseAnalytics.Param.CURRENCY, "USD") }
        verify { anyConstructed<Bundle>().putString(FirebaseAnalytics.Param.TRANSACTION_ID, "txn-1") }
        verify { anyConstructed<Bundle>().putLong(FirebaseAnalytics.Param.QUANTITY, 2L) }
        verify { firebase.logEvent(FirebaseAnalytics.Event.PURCHASE, any()) }
    }

    @Test
    fun `given a purchase when tracking then the event's own parameters travel too`() = runTest {
        PurchaseFirebaseEventTracker(purchase, firebase).track()

        verify { anyConstructed<Bundle>().putString("ryder_id", "r1") }
        verify { anyConstructed<Bundle>().putString("fare", "Adult") }
        verify { anyConstructed<Bundle>().putLong("count", 2L) }
    }
}

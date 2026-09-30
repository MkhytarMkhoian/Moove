package com.moove.inspector.analytics.vendor

import com.adjust.sdk.AdjustInstance
import com.moove.inspector.analytics.event.AdImpressionSimulated
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.trackers.AdRevenueEventTracker
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class InspectorAdjustEventTrackerFactoryTest {

    private val adjust: AdjustInstance = mockk(relaxed = true)
    private val factory = InspectorAdjustEventTrackerFactory(adjust)

    @Test
    fun `given the simulated ad impression when routing then it goes through Adjust's ad-revenue API`() {
        assertIs<AdRevenueEventTracker>(factory.create(AdImpressionSimulated(revenue = 0.0042)).handlers.single())
    }

    @Test
    fun `given the simulated ad impression when tracking then Adjust gets its source, network, unit and revenue`() = runTest {
        factory.create(AdImpressionSimulated(revenue = 0.0042)).handlers.single().track()

        verify {
            adjust.trackAdRevenue(withArg {
                assertEquals("applovin_max_sdk", it.source)
                assertEquals(0.0042, it.revenue)
                assertEquals("USD", it.currency)
                assertEquals("Demo Network", it.adRevenueNetwork)
                assertEquals("home_banner", it.adRevenueUnit)
                assertEquals(1, it.adImpressionsCount)
            })
        }
    }

    @Test
    fun `given anything else when routing then it is declined`() {
        assertEquals(Resolution.Declined, factory.create(object : Event { override val name = "inspector_opened" }))
    }
}

package com.moove.inspector.analytics.vendor

import com.appsflyer.AppsFlyerLib
import com.appsflyer.share.AFAdRevenueData
import com.appsflyer.share.MediationNetwork
import com.moove.inspector.analytics.event.AdImpressionSimulated
import com.moove.inspector.analytics.vendor.appsflyer.toAppsFlyerAdRevenueEvent
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.appsflyer.toAFAdRevenueData
import io.github.mkhytarmkhoian.herald.appsflyer.trackers.AdRevenueEventTracker
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class InspectorAppsFlyerEventTrackerFactoryTest {

    private val factory = InspectorAppsFlyerEventTrackerFactory(mockk<AppsFlyerLib>(relaxed = true))

    @Test
    fun `given the simulated ad impression when routing then it goes through AppsFlyer's ad-revenue API`() {
        assertIs<AdRevenueEventTracker>(factory.create(AdImpressionSimulated(revenue = 0.0042)).handlers.single())
    }

    @Test
    fun `given the simulated ad impression when shaped for AppsFlyer then its network, revenue and currency carry over`() {
        assertEquals(
            AFAdRevenueData("Demo Network", MediationNetwork.APPLOVIN_MAX, "USD", 0.0042),
            AdImpressionSimulated(revenue = 0.0042).toAppsFlyerAdRevenueEvent().toAFAdRevenueData(),
        )
    }

    @Test
    fun `given anything else when routing then it is declined`() {
        assertEquals(Resolution.Declined, factory.create(object : Event { override val name = "inspector_opened" }))
    }
}

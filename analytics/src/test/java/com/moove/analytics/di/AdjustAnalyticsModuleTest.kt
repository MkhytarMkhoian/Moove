package com.moove.analytics.di

import com.adjust.sdk.AdjustInstance
import io.github.mkhytarmkhoian.herald.AnalyticsValue
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Property
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.UnhandledPropertyException
import io.github.mkhytarmkhoian.herald.adjust.AdRevenueEvent
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTracker
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetter
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetterFactory
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** The Adjust chains: events are opt-in through a feature factory, properties are strict. */
class AdjustAnalyticsModuleTest {

    private val adjust: AdjustInstance = mockk(relaxed = true)

    private val plain = object : Event { override val name = "plain" }
    private val property = object : Property {
        override val name = "seat_preference"
        override val value = AnalyticsValue.String("aisle")
    }

    @Test
    fun `given no feature factory when tracking an event then nothing is sent`() = runTest {
        adjustTracker(adjust, emptyList(), emptyList()).track(plain)

        verify(exactly = 0) { adjust.trackEvent(any()) }
    }

    @Test
    fun `given no feature factory when tracking ad revenue then Herald's own factory sends it`() = runTest {
        val adRevenue = object : AdRevenueEvent {
            override val name = "ad_impression"
            override val source = "admob_sdk"
            override val revenue = 0.01
            override val currency = "USD"
        }

        adjustTracker(adjust, emptyList(), emptyList()).track(adRevenue)

        verify { adjust.trackAdRevenue(any()) }
    }

    @Test
    fun `given a feature event factory when tracking then Dropped stops the chain, Claimed sends, Declined is silent`() = runTest {
        val seen = mutableListOf<String>()
        val contributed = AdjustEventTrackerFactory {
            when (it.name) {
                "noise" -> Resolution.Dropped
                "with_token" -> Resolution.Claimed(AdjustEventTracker { seen += "sent" })
                else -> Resolution.Declined
            }
        }
        val tracker = adjustTracker(adjust, listOf(contributed), emptyList())

        tracker.track(object : Event { override val name = "noise" })
        tracker.track(object : Event { override val name = "with_token" })
        tracker.track(plain)

        assertEquals(listOf("sent"), seen)
        verify(exactly = 0) { adjust.trackEvent(any()) }
    }

    @Test
    fun `given no feature factory when setting a property then it throws instead of registering silently`() = runTest {
        val tracker = adjustTracker(adjust, emptyList(), emptyList())

        assertFailsWith<UnhandledPropertyException> { tracker.set(property) }
        verify(exactly = 0) { adjust.addGlobalCallbackParameter(any(), any()) }
    }

    @Test
    fun `given a feature property factory when setting then its claim is honoured and an unclaimed property still throws`() = runTest {
        val seen = mutableListOf<String>()
        val contributed = AdjustPropertySetterFactory {
            if (it.name == "plan") Resolution.Claimed(AdjustPropertySetter { seen += "plan" }) else Resolution.Declined
        }
        val plan = object : Property {
            override val name = "plan"
            override val value = AnalyticsValue.String("pro")
        }
        val tracker = adjustTracker(adjust, emptyList(), listOf(contributed))

        tracker.set(plan)
        assertFailsWith<UnhandledPropertyException> { tracker.set(property) }

        assertEquals(listOf("plan"), seen)
    }
}

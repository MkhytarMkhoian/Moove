package com.moove.analytics.di

import com.mixpanel.android.mpmetrics.MixpanelAPI
import io.github.mkhytarmkhoian.herald.AnalyticsValue
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Property
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.ScreenViewEvent
import io.github.mkhytarmkhoian.herald.UserProperty
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelEventTrackerFactory
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelPropertySetter
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelPropertySetterFactory
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/** The Mixpanel chains: screen views dropped first, then feature factories, then Herald's. */
class MixpanelAnalyticsModuleTest {

    private val mixpanel: MixpanelAPI = mockk(relaxed = true)

    private val screenView = object : ScreenViewEvent {
        override val name = "home_shown"
        override val screenName = "Home"
    }
    private val plain = object : Event { override val name = "plain" }
    private val userProperty = object : UserProperty {
        override val name = "plan"
        override val value = AnalyticsValue.String("pro")
    }
    private val property = object : Property {
        override val name = "theme"
        override val value = AnalyticsValue.String("dark")
    }

    @Test
    fun `given no feature factory when setting properties then a UserProperty goes to the profile and a plain one becomes a super property`() = runTest {
        val tracker = mixpanelTracker(mixpanel, emptyList(), emptyList())

        tracker.set(userProperty)
        tracker.set(property)

        val people = mixpanel.people
        verify { people.set("plan", "pro") }
        verify { mixpanel.updateSuperProperties(any()) }
        verify(exactly = 0) { people.set("theme", any()) }
    }

    @Test
    fun `given a feature property factory when setting a property it claims then Herald's factories never see it`() = runTest {
        val seen = mutableListOf<String>()
        val contributed = MixpanelPropertySetterFactory {
            if (it.name == "plan") Resolution.Claimed(MixpanelPropertySetter { seen += "mine" }) else Resolution.Declined
        }

        mixpanelTracker(mixpanel, emptyList(), listOf(contributed)).set(userProperty)

        assertEquals(listOf("mine"), seen)
        val people = mixpanel.people
        verify(exactly = 0) { people.set(any<String>(), any()) }
    }

    @Test
    fun `given a feature event factory when tracking then a screen view is dropped before it and a plain event goes through`() = runTest {
        val seen = mutableListOf<String>()
        val contributed = MixpanelEventTrackerFactory { event ->
            seen += event.name
            Resolution.Declined
        }
        val tracker = mixpanelTracker(mixpanel, listOf(contributed), emptyList())

        tracker.track(screenView)
        tracker.track(plain)

        assertEquals(listOf("plain"), seen)
        verify(exactly = 0) { mixpanel.trackMap("home_shown", any()) }
        verify { mixpanel.trackMap("plain", any()) }
    }
}

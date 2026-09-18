package com.moove.analytics.di

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.ScreenViewEvent
import io.github.mkhytarmkhoian.herald.firebase.FirebaseEventTracker
import io.github.mkhytarmkhoian.herald.firebase.FirebaseEventTrackerFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/** The Firebase chain: feature factories first, Herald's screen view next, the generic terminator last. */
class FirebaseAnalyticsModuleTest {

    private val firebase: FirebaseAnalytics = mockk(relaxed = true)

    private val screenView = object : ScreenViewEvent {
        override val name = "home_shown"
        override val screenName = "Home"
    }
    private val plain = object : Event { override val name = "plain" }

    @Before
    fun stubBundle() {
        mockkConstructor(Bundle::class)
        every { anyConstructed<Bundle>().putString(any(), any()) } returns Unit
    }

    @After
    fun unstubBundle() = unmockkConstructor(Bundle::class)

    @Test
    fun `given a feature factory that claims screen views when tracking one then Herald's screen view factory never sees it`() = runTest {
        val seen = mutableListOf<String>()
        val override = FirebaseEventTrackerFactory {
            if (it is ScreenViewEvent) Resolution.Claimed(FirebaseEventTracker { seen += "mine:${it.screenName}" })
            else Resolution.Declined
        }

        firebaseTracker(firebase, listOf(override), emptyList()).track(screenView)

        assertEquals(listOf("mine:Home"), seen)
        verify(exactly = 0) { firebase.logEvent(any(), any()) }
    }

    @Test
    fun `given no feature factory when tracking a plain event then it reaches Firebase under its own name`() = runTest {
        firebaseTracker(firebase, emptyList(), emptyList()).track(plain)

        verify { firebase.logEvent("plain", any()) }
    }

    @Test
    fun `given no feature factory when tracking a screen view then it goes out as GA4's reserved event`() = runTest {
        firebaseTracker(firebase, emptyList(), emptyList()).track(screenView)

        verify { firebase.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, any()) }
    }
}

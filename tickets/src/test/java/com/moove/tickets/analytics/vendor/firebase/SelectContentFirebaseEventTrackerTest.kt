package com.moove.tickets.analytics.vendor.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.moove.tickets.analytics.event.RyderSelected
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SelectContentFirebaseEventTrackerTest {

    private val firebase: FirebaseAnalytics = mockk(relaxed = true)

    @Before
    fun stubBundle() {
        mockkConstructor(Bundle::class)
        every { anyConstructed<Bundle>().putString(any(), any()) } returns Unit
    }

    @After
    fun unstubBundle() = unmockkConstructor(Bundle::class)

    @Test
    fun `given a content event when tracking then GA4's select_content carries type and id`() = runTest {
        SelectContentFirebaseEventTracker(RyderSelected("Adult"), firebase).track()

        verify { anyConstructed<Bundle>().putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ryder") }
        verify { anyConstructed<Bundle>().putString(FirebaseAnalytics.Param.ITEM_ID, "Adult") }
        verify { firebase.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, any()) }
        verify(exactly = 0) { firebase.logEvent("ryder_selected", any()) }
    }
}

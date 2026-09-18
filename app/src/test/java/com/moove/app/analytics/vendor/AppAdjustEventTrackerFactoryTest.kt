package com.moove.app.analytics.vendor

import com.adjust.sdk.AdjustInstance
import com.moove.app.analytics.event.DeepLinkOpened
import com.moove.app.analytics.event.HomeScreenViewed
import com.moove.app.analytics.event.SectionSelected
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.trackers.TokenEventTracker
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AppAdjustEventTrackerFactoryTest {

    private val adjust: AdjustInstance = mockk(relaxed = true)
    private val factory = AppAdjustEventTrackerFactory(adjust)

    @Test
    fun `given a deep link when routing then Herald's token tracker is the handler`() {
        val resolution = factory.create(DeepLinkOpened(host = "moove.app", path = "/movies/42"))

        assertIs<TokenEventTracker>(resolution.handlers.single())
    }

    @Test
    fun `given a deep link when tracking then Adjust gets the token with host and path as callback parameters`() = runTest {
        factory.create(DeepLinkOpened(host = "moove.app", path = "/movies/42")).handlers.single().track()

        verify {
            adjust.trackEvent(withArg {
                assertEquals("dl0001", it.eventToken)
                assertEquals(mapOf("host" to "moove.app", "path" to "/movies/42"), it.callbackParameters)
            })
        }
    }

    @Test
    fun `given anything else the app reports when routing then it is declined`() {
        assertEquals(Resolution.Declined, factory.create(HomeScreenViewed))
        assertEquals(Resolution.Declined, factory.create(SectionSelected("movies")))
    }
}

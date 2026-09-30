package com.moove.analytics.vendor.appsflyer

import android.content.Context
import com.appsflyer.AppsFlyerLib
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.parameters
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class MappedAppsFlyerEventTrackerFactoryTest {

    private data class SignedUp(val method: String) : Event {
        override val name = "signed_up"
        override val parameters = parameters { put("method", method) }
    }

    private open class Opened : Event {
        override val name = "opened"
    }

    private class OpenedFromPush : Opened()

    private val appsFlyer: AppsFlyerLib = mockk(relaxed = true)
    private val context: Context = mockk()
    private val factory = MappedAppsFlyerEventTrackerFactory(
        eventNames = mapOf(
            SignedUp::class to "af_complete_registration",
            Opened::class to "af_opened",
        ),
        appsFlyer = appsFlyer,
        context = context,
    )

    @Test
    fun `given a mapped event when tracking then AppsFlyer gets the mapped name with the event's own values`() = runTest {
        factory.create(SignedUp(method = "email")).handlers.single().track()

        verify { appsFlyer.logEvent(context, "af_complete_registration", mapOf("method" to "email")) }
    }

    @Test
    fun `given an unmapped event when routing then it is declined`() {
        val unmapped = object : Event {
            override val name = "unmapped"
        }

        assertEquals(Resolution.Declined, factory.create(unmapped))
    }

    @Test
    fun `given a subtype of a mapped event when routing then it is declined, as matching is by exact class`() {
        assertEquals(Resolution.Declined, factory.create(OpenedFromPush()))
    }
}

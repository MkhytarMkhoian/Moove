package com.moove.analytics

import io.github.mkhytarmkhoian.herald.AnalyticsOperation
import io.github.mkhytarmkhoian.herald.AnalyticsValue
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Herald
import io.github.mkhytarmkhoian.herald.Identity
import io.github.mkhytarmkhoian.herald.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RecentEventsProviderTest {

    private var now = 0L
    private val recent = RecentEventsProvider(capacity = 3, clock = { ++now })

    private fun event(name: String) = object : Event {
        override val name = name
        override val parameters = parameters { put("n", 1) }
    }

    @Test
    fun `Entries are newest first and capped at capacity`() = runTest {
        recent.track(event("a"))
        recent.track(event("b"))
        recent.track(event("c"))
        recent.track(event("d"))

        assertEquals(listOf("event d { n = 1 }", "event c { n = 1 }", "event b { n = 1 }"), recent.entries.value.map { it.summary })
    }

    @Test
    fun `Every capability lands in the same timeline`() = runTest {
        val provider = RecentEventsProvider(clock = { ++now })
        provider.start()
        provider.setEnabled(true)
        provider.identify(Identity("demo"))
        provider.set(object : io.github.mkhytarmkhoian.herald.Property {
            override val name = "plan"
            override val value = AnalyticsValue.String("pro")
        })
        provider.flush()
        provider.reset()

        assertEquals(
            listOf("reset", "flush", "property plan = pro", "identify demo", "enabled true", "start"),
            provider.entries.value.map { it.summary },
        )
    }

    @Test
    fun `A vendor failure reported by Herald shows up next to the call that failed`() = runTest {
        val provider = RecentEventsProvider(clock = { ++now })
        val herald = Herald {
            provider(name = "inspector", events = provider)
            provider(name = "broken", events = { throw IllegalStateException("vendor down") })
            dispatcher(Dispatchers.Unconfined)
            errorReporter { name, operation, failure -> provider.recordFailure(name, operation, failure) }
        }

        herald.track(event("checkout"))

        val (failed, tracked) = provider.entries.value
        assertIs<RecentEventsProvider.Entry.Failed>(failed)
        assertEquals("broken", failed.provider)
        assertEquals(AnalyticsOperation.Track("checkout"), failed.operation)
        assertIs<RecentEventsProvider.Entry.Tracked>(tracked)
    }
}

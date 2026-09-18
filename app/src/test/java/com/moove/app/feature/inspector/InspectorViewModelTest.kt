package com.moove.app.feature.inspector

import com.moove.analytics.RecentEventsProvider
import com.moove.app.feature.inspector.model.InspectorEntryModel
import com.moove.core.exception.ExceptionHandler
import io.github.mkhytarmkhoian.herald.AnalyticsOperation
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.testing.AnalyticsRecord
import io.github.mkhytarmkhoian.herald.testing.FakeAnalyticsProvider
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InspectorViewModelTest {

    private var now = 0L
    private val recentEvents = RecentEventsProvider(clock = { ++now })
    private val analytics = FakeAnalyticsProvider()

    private fun createViewModel() = InspectorViewModel(
        exceptionHandler = ExceptionHandler { },
        recentEvents = recentEvents,
        analyticsEventService = analytics,
        analyticsLifecycleService = analytics,
    )

    @Test
    fun `given recorded calls when created then the state mirrors the timeline, newest first`() = runTest {
        recentEvents.track(object : Event { override val name = "home_shown" })
        recentEvents.recordFailure("adjust", AnalyticsOperation.SetProperty("seat_preference"), IllegalStateException("unmapped"))

        createViewModel().test(this) {
            runOnCreate()
            expectInitialState()
            expectState {
                copy(
                    entries = listOf(
                        InspectorEntryModel(at = 2L, summary = "FAILED adjust on SetProperty(propertyName=seat_preference): unmapped", failed = true),
                        InspectorEntryModel(at = 1L, summary = "event home_shown", failed = false),
                    ),
                )
            }
            // the timeline is collected for as long as the screen is subscribed
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun `given the simulate button when tapped then an ad impression is tracked`() = runTest {
        createViewModel().test(this) {
            expectInitialState()
            containerHost.onSimulateAdImpression()
        }

        analytics.assertTracked("ad_impression")
    }

    @Test
    fun `given the flush button when tapped then the lifecycle is flushed`() = runTest {
        createViewModel().test(this) {
            expectInitialState()
            containerHost.onFlush()
        }

        assertEquals(listOf(AnalyticsRecord.Flushed), analytics.records)
    }

    @Test
    fun `given the clear button when tapped then the provider is emptied`() = runTest {
        recentEvents.track(object : Event { override val name = "home_shown" })

        createViewModel().test(this) {
            expectInitialState()
            containerHost.onClear()
        }

        assertTrue(recentEvents.entries.value.isEmpty())
    }

    @Test
    fun `given back when tapped then GoBack is posted`() = runTest {
        createViewModel().test(this) {
            expectInitialState()
            containerHost.onBack()
            expectSideEffect(InspectorEffect.GoBack)
        }
    }
}

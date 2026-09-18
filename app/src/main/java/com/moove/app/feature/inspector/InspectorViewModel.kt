package com.moove.app.feature.inspector

import androidx.lifecycle.ViewModel
import com.moove.analytics.RecentEventsProvider
import com.moove.app.analytics.event.AdImpressionSimulated
import com.moove.app.feature.inspector.model.asPresentation
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import io.github.mkhytarmkhoian.herald.AnalyticsLifecycleService
import io.github.mkhytarmkhoian.herald.EventTrackerService
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Mirrors the in-house provider's timeline into state and offers the two calls no screen would
 * otherwise make: a simulated ad impression (the `AdRevenueEvent` path) and a manual flush.
 */
class InspectorViewModel(
    exceptionHandler: ExceptionHandler,
    private val recentEvents: RecentEventsProvider,
    private val analyticsEventService: EventTrackerService,
    private val analyticsLifecycleService: AnalyticsLifecycleService,
) : ViewModel(), ContainerHost<InspectorState, InspectorEffect> {

    override val container: Container<InspectorState, InspectorEffect> = container(
        initialState = InspectorState(),
        buildSettings = {
            this.exceptionHandler = exceptionHandler.asCoroutineExceptionHandler()
        },
    ) {
        repeatOnSubscription {
            recentEvents.entries.collect { entries ->
                reduce { state.copy(entries = entries.asPresentation()) }
            }
        }
    }

    fun onSimulateAdImpression() = intent {
        analyticsEventService.track(AdImpressionSimulated(revenue = 0.0042))
    }

    fun onFlush() = intent { analyticsLifecycleService.flush() }

    fun onClear() = intent { recentEvents.clear() }

    fun onBack() = intent { postSideEffect(InspectorEffect.GoBack) }
}

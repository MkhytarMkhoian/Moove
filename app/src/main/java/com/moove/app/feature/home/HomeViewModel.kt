package com.moove.app.feature.home

import androidx.lifecycle.ViewModel
import com.moove.analytics.domain.use_cases.GetAnalyticsConsentUseCase
import com.moove.analytics.domain.use_cases.SetAnalyticsConsentUseCase
import com.moove.app.analytics.event.HomeScreenViewed
import com.moove.app.analytics.property.PreferredSection
import com.moove.app.analytics.event.SectionSelected
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.shared.presentation.viewmodel.executeUseCase
import io.github.mkhytarmkhoian.herald.EventTrackerService
import io.github.mkhytarmkhoian.herald.IdentifiableUserService
import io.github.mkhytarmkhoian.herald.Identity
import io.github.mkhytarmkhoian.herald.PropertyTrackerService
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(
    private val exceptionHandler: ExceptionHandler,
    private val analyticsEventService: EventTrackerService,
    private val analyticsPropertyService: PropertyTrackerService,
    private val analyticsIdentityService: IdentifiableUserService,
    private val getAnalyticsConsentUseCase: GetAnalyticsConsentUseCase,
    private val setAnalyticsConsentUseCase: SetAnalyticsConsentUseCase,
) : ViewModel(), ContainerHost<HomeState, HomeEffect> {

    override val container: Container<HomeState, HomeEffect> = container(
        initialState = HomeState(),
        buildSettings = {
            this.exceptionHandler = this@HomeViewModel.exceptionHandler.asCoroutineExceptionHandler()
        },
    ) {
        analyticsEventService.track(HomeScreenViewed)
        executeUseCase(exceptionHandler) { getAnalyticsConsentUseCase() }
            .onSuccess { enabled -> reduce { state.copy(analyticsEnabled = enabled) } }
    }

    fun onRyderClick() = intent {
        analyticsEventService.track(SectionSelected(SECTION_TICKETS))
        analyticsPropertyService.set(PreferredSection(SECTION_TICKETS))
        postSideEffect(HomeEffect.GoToRyderList)
    }

    fun onMoviesClick() = intent {
        analyticsEventService.track(SectionSelected(SECTION_MOVIES))
        analyticsPropertyService.set(PreferredSection(SECTION_MOVIES))
        postSideEffect(HomeEffect.GoToMovieList)
    }

    fun onInspectorClick() = intent { postSideEffect(HomeEffect.GoToInspector) }

    fun onAnalyticsToggled(enabled: Boolean) = intent {
        executeUseCase(exceptionHandler) { setAnalyticsConsentUseCase(enabled) }
            .onSuccess { reduce { state.copy(analyticsEnabled = enabled) } }
    }

    fun onSignInClick() = intent {
        analyticsIdentityService.identify(Identity(DEMO_USER))
        reduce { state.copy(signedInUserId = DEMO_USER) }
    }

    fun onSignOutClick() = intent {
        analyticsIdentityService.reset()
        reduce { state.copy(signedInUserId = null) }
    }

    companion object {
        const val DEMO_USER = "demo-user"
        private const val SECTION_TICKETS = "tickets"
        private const val SECTION_MOVIES = "movies"
    }
}

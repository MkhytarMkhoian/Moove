package com.moove.app.feature.home

import com.moove.analytics.domain.use_cases.GetAnalyticsConsentUseCase
import com.moove.analytics.domain.use_cases.SetAnalyticsConsentUseCase
import com.moove.core.exception.ExceptionHandler
import io.github.mkhytarmkhoian.herald.Identity
import io.github.mkhytarmkhoian.herald.testing.AnalyticsRecord
import io.github.mkhytarmkhoian.herald.testing.FakeAnalyticsProvider
import io.mockk.coVerify
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test
import kotlin.test.assertEquals

class HomeViewModelTest {

    private val analytics = FakeAnalyticsProvider()
    private val getAnalyticsConsentUseCase: GetAnalyticsConsentUseCase = mockk {
        coEvery { this@mockk() } returns true
    }
    private val setAnalyticsConsentUseCase: SetAnalyticsConsentUseCase = mockk(relaxed = true)

    private fun viewModel() = HomeViewModel(
        exceptionHandler = ExceptionHandler { },
        analyticsEventService = analytics,
        analyticsPropertyService = analytics,
        analyticsIdentityService = analytics,
        getAnalyticsConsentUseCase = getAnalyticsConsentUseCase,
        setAnalyticsConsentUseCase = setAnalyticsConsentUseCase,
    )

    @Test
    fun `given a section click then the event is tracked and the profile property set`() = runTest {
        viewModel().test(this) {
            containerHost.onRyderClick()
            expectSideEffect(HomeEffect.GoToRyderList)
        }

        analytics.assertTracked("section_selected") { param("section", "tickets") }
        analytics.assertPropertySet("preferred_section", "tickets")
    }

    @Test
    fun `given the consent switch then the decision goes through the use case and the state follows`() = runTest {
        viewModel().test(this) {
            containerHost.onAnalyticsToggled(true)
            expectState { copy(analyticsEnabled = true) }
        }

        coVerify { setAnalyticsConsentUseCase(true) }
    }

    @Test
    fun `given sign in then sign out then identify precedes reset`() = runTest {
        viewModel().test(this) {
            containerHost.onSignInClick()
            expectState { copy(signedInUserId = HomeViewModel.DEMO_USER) }
            containerHost.onSignOutClick()
            expectState { copy(signedInUserId = null) }
        }

        assertEquals(
            listOf(AnalyticsRecord.Identified(Identity(HomeViewModel.DEMO_USER)), AnalyticsRecord.Reset),
            analytics.records.filter { it is AnalyticsRecord.Identified || it == AnalyticsRecord.Reset },
        )
    }

    @Test
    fun `given creation then the home screen view is tracked once and the stored consent is loaded`() = runTest {
        viewModel().test(this) {
            runOnCreate()
            expectInitialState()
            expectState { copy(analyticsEnabled = true) }
        }

        analytics.assertTracked("home_shown")
        analytics.assertNothingElseTracked()
    }
}

package com.moove.app.main

import com.moove.analytics.domain.use_cases.StartAnalyticsUseCase
import com.moove.core.exception.ExceptionHandler
import com.moove.shared.feature.deeplink.domain.GetDeeplinkUseCase
import io.github.mkhytarmkhoian.herald.testing.FakeAnalyticsProvider
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test

class MainActivityViewModelTest {

    private val startAnalyticsUseCase: StartAnalyticsUseCase = mockk(relaxed = true)
    private val getDeeplinkUseCase: GetDeeplinkUseCase = mockk(relaxed = true)
    private val analytics = FakeAnalyticsProvider()

    private fun viewModel() = MainActivityViewModel(
        exceptionHandler = ExceptionHandler { },
        getDeeplinkUseCase = getDeeplinkUseCase,
        startAnalyticsUseCase = startAnalyticsUseCase,
        analyticsEventService = analytics,
    )

    @Test
    fun `given the app's entry point is created then analytics is started once`() = runTest {
        viewModel().test(this) {
            runOnCreate()
            expectInitialState()
        }

        coVerify(exactly = 1) { startAnalyticsUseCase() }
        analytics.assertNothingElseTracked()
    }

    @Test
    fun `given a null intent when handling it then nothing is resolved`() = runTest {
        viewModel().test(this) {
            expectInitialState()
            containerHost.handleIntent(null)
        }

        coVerify(exactly = 0) { getDeeplinkUseCase(any()) }
    }
}

package com.moove.analytics.domain.use_cases

import io.github.mkhytarmkhoian.herald.testing.AnalyticsRecord
import io.github.mkhytarmkhoian.herald.testing.FakeAnalyticsProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class StartAnalyticsUseCaseTest {

    private val analytics = FakeAnalyticsProvider()
    private val restoreAnalyticsConsentUseCase: RestoreAnalyticsConsentUseCase = mockk {
        coEvery { this@mockk() } coAnswers { analytics.setEnabled(true) }
    }
    private val useCase = StartAnalyticsUseCase(analytics, restoreAnalyticsConsentUseCase)

    @Test
    fun `given a process start when invoked then the vendors start and only then is consent restored`() = runTest {
        useCase()

        assertEquals(listOf(AnalyticsRecord.Started, AnalyticsRecord.EnabledSet(true)), analytics.records)
    }
}

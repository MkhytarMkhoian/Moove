package com.moove.analytics.domain.use_cases

import com.moove.analytics.domain.AnalyticsConsentRepository
import io.github.mkhytarmkhoian.herald.testing.AnalyticsRecord
import io.github.mkhytarmkhoian.herald.testing.FakeAnalyticsProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RestoreAnalyticsConsentUseCaseTest {

    private val analytics = FakeAnalyticsProvider()
    private val repository: AnalyticsConsentRepository = mockk()
    private val useCase = RestoreAnalyticsConsentUseCase(repository, analytics)

    @Test
    fun `given consent is stored off when invoked then the vendors are told off, explicitly`() = runTest {
        coEvery { repository.isEnabled() } returns false

        useCase()

        assertEquals(listOf(AnalyticsRecord.EnabledSet(false)), analytics.records)
    }

    @Test
    fun `given consent is stored on when invoked then the vendors are told on`() = runTest {
        coEvery { repository.isEnabled() } returns true

        useCase()

        assertEquals(listOf(AnalyticsRecord.EnabledSet(true)), analytics.records)
    }
}

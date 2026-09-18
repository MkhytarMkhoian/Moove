package com.moove.analytics.domain.use_cases

import com.moove.analytics.domain.AnalyticsConsentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertTrue

class GetAnalyticsConsentUseCaseTest {

    private val repository: AnalyticsConsentRepository = mockk()
    private val useCase = GetAnalyticsConsentUseCase(repository)

    @Test
    fun `given a stored decision when invoked then it is returned`() = runTest {
        coEvery { repository.isEnabled() } returns true

        val result = useCase()

        assertTrue(result)
        coVerify { repository.isEnabled() }
    }
}

package com.moove.analytics.domain.use_cases

import com.moove.analytics.domain.AnalyticsConsentRepository
import io.github.mkhytarmkhoian.herald.ConsentService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class SetAnalyticsConsentUseCaseTest {

    private val order = mutableListOf<String>()
    private val repository: AnalyticsConsentRepository = mockk {
        coEvery { setEnabled(any()) } coAnswers { order += "repository" }
    }
    private val service: ConsentService = mockk {
        coEvery { setEnabled(any()) } coAnswers { order += "vendors" }
    }
    private val useCase = SetAnalyticsConsentUseCase(repository, service)

    @Test
    fun `given a decision when invoked then it is persisted before the vendors hear of it`() = runTest {
        useCase(true)

        assertEquals(listOf("repository", "vendors"), order)
    }
}

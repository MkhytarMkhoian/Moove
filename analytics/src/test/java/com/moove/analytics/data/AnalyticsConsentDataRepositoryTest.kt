package com.moove.analytics.data

import com.moove.analytics.data.local.AnalyticsConsentLocalDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertTrue

class AnalyticsConsentDataRepositoryTest {

    private val localDataSource: AnalyticsConsentLocalDataSource = mockk(relaxed = true)
    private val repository = AnalyticsConsentDataRepository(
        localDataSource = localDataSource,
        backgroundDispatcher = Dispatchers.Unconfined,
    )

    @Test
    fun `given a stored decision when reading then the local source answers`() = runTest {
        every { localDataSource.isEnabled() } returns true

        assertTrue(repository.isEnabled())
    }

    @Test
    fun `given a decision when writing then the local source stores it`() = runTest {
        repository.setEnabled(true)

        verify { localDataSource.setEnabled(true) }
    }
}

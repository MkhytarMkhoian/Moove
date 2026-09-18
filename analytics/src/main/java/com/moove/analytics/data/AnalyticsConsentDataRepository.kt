package com.moove.analytics.data

import com.moove.analytics.data.local.AnalyticsConsentLocalDataSource
import com.moove.analytics.domain.AnalyticsConsentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AnalyticsConsentDataRepository(
    private val localDataSource: AnalyticsConsentLocalDataSource,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AnalyticsConsentRepository {

    override suspend fun isEnabled(): Boolean = withContext(backgroundDispatcher) {
        localDataSource.isEnabled()
    }

    override suspend fun setEnabled(enabled: Boolean) = withContext(backgroundDispatcher) {
        localDataSource.setEnabled(enabled)
    }
}

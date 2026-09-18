package com.moove.analytics.domain

interface AnalyticsConsentRepository {
    suspend fun isEnabled(): Boolean
    suspend fun setEnabled(enabled: Boolean)
}

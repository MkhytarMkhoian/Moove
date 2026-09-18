package com.moove.analytics.domain.use_cases

import com.moove.analytics.domain.AnalyticsConsentRepository

class GetAnalyticsConsentUseCase(
    private val analyticsConsentRepository: AnalyticsConsentRepository,
) {
    suspend operator fun invoke(): Boolean = analyticsConsentRepository.isEnabled()
}

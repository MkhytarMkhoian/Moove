package com.moove.analytics.domain.use_cases

import com.moove.analytics.domain.AnalyticsConsentRepository
import io.github.mkhytarmkhoian.herald.ConsentService

/** Persisted first, so a crash between the two leaves the vendors behind, never ahead. */
class SetAnalyticsConsentUseCase(
    private val analyticsConsentRepository: AnalyticsConsentRepository,
    private val analyticsConsentService: ConsentService,
) {
    suspend operator fun invoke(enabled: Boolean) {
        analyticsConsentRepository.setEnabled(enabled)
        analyticsConsentService.setEnabled(enabled)
    }
}

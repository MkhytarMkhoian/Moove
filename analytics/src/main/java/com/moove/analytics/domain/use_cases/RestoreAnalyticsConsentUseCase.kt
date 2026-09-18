package com.moove.analytics.domain.use_cases

import com.moove.analytics.domain.AnalyticsConsentRepository
import io.github.mkhytarmkhoian.herald.ConsentService

/**
 * Re-applies the stored decision to the vendors. Needed on every start: Herald keeps no state of
 * its own and Adjust's `start()` resets its enabled flag.
 */
class RestoreAnalyticsConsentUseCase(
    private val analyticsConsentRepository: AnalyticsConsentRepository,
    private val analyticsConsentService: ConsentService,
) {
    suspend operator fun invoke() {
        analyticsConsentService.setEnabled(analyticsConsentRepository.isEnabled())
    }
}

package com.moove.analytics.domain.use_cases

import io.github.mkhytarmkhoian.herald.AnalyticsLifecycleService

/**
 * Starts the vendors and then re-applies the stored consent, in that order: Herald's Adjust
 * adapter disables the SDK inside `start()` so a fresh install collects nothing before consent,
 * which also discards whatever the user decided last time. Firebase and Mixpanel remember on
 * their own, and applying a decision twice costs nothing.
 */
class StartAnalyticsUseCase(
    private val analyticsLifecycleService: AnalyticsLifecycleService,
    private val restoreAnalyticsConsentUseCase: RestoreAnalyticsConsentUseCase,
) {
    suspend operator fun invoke() {
        analyticsLifecycleService.start()
        restoreAnalyticsConsentUseCase()
    }
}

package com.moove.analytics.di

import android.util.Log
import com.moove.analytics.RecentEventsProvider
import io.github.mkhytarmkhoian.herald.AnalyticsErrorReporter
import io.github.mkhytarmkhoian.herald.Herald
import org.koin.core.qualifier.named
import org.koin.dsl.module

val inspectorAnalyticsModule = module {

    single { RecentEventsProvider() }

    single<Herald.Provider>(named("inspector")) {
        val recent = get<RecentEventsProvider>()
        Herald.Provider(
            name = "inspector",
            events = recent,
            properties = recent,
            identity = recent,
            lifecycle = recent,
            consent = recent,
        )
    }

    single<AnalyticsErrorReporter> {
        val recent = get<RecentEventsProvider>()
        AnalyticsErrorReporter { provider, operation, failure ->
            Log.e(ANALYTICS_LOG_TAG, "$provider failed on $operation", failure)
            recent.recordFailure(provider, operation, failure)
        }
    }
}

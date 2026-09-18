package com.moove.analytics.di

import android.util.Log
import io.github.mkhytarmkhoian.herald.Herald
import io.github.mkhytarmkhoian.herald.log.AnalyticsLogger
import io.github.mkhytarmkhoian.herald.log.CompositeLogEventTrackerFactory
import io.github.mkhytarmkhoian.herald.log.CompositeLogPropertySetterFactory
import io.github.mkhytarmkhoian.herald.log.GenericLogEventTrackerFactory
import io.github.mkhytarmkhoian.herald.log.GenericLogPropertySetterFactory
import io.github.mkhytarmkhoian.herald.log.LogAnalyticsService
import io.github.mkhytarmkhoian.herald.log.LogAnalyticsTrackerService
import io.github.mkhytarmkhoian.herald.log.ScreenViewLogEventTrackerFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal const val ANALYTICS_LOG_TAG = "analytics"

val logAnalyticsModule = module {

    single<AnalyticsLogger> { AnalyticsLogger { Log.d(ANALYTICS_LOG_TAG, it) } }

    single<Herald.Provider>(named("log")) {
        val tracker = logTracker(get())
        val service = LogAnalyticsService(get())
        Herald.Provider(
            name = "log",
            events = tracker,
            properties = tracker,
            identity = service,
            lifecycle = service,
            consent = service,
        )
    }
}

internal fun logTracker(logger: AnalyticsLogger) = LogAnalyticsTrackerService(
    eventTrackerFactory = CompositeLogEventTrackerFactory(
        ScreenViewLogEventTrackerFactory(logger),
        GenericLogEventTrackerFactory(logger),
    ),
    propertySetterFactory = CompositeLogPropertySetterFactory(
        GenericLogPropertySetterFactory(logger),
    ),
)

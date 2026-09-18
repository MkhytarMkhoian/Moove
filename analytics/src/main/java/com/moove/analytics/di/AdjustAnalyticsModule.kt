package com.moove.analytics.di

import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.AdjustInstance
import com.adjust.sdk.LogLevel
import io.github.mkhytarmkhoian.herald.Herald
import io.github.mkhytarmkhoian.herald.adjust.AdRevenueAdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.AdjustAnalyticsService
import io.github.mkhytarmkhoian.herald.adjust.AdjustAnalyticsTrackerService
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetterFactory
import io.github.mkhytarmkhoian.herald.adjust.CompositeAdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.CompositeAdjustPropertySetterFactory
import io.github.mkhytarmkhoian.herald.adjust.RequireMappedAdjustPropertySetterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

// A placeholder: sandbox accepts the format and rejects the account, which is enough to exercise
// the adapter end to end from Logcat.
private const val APP_TOKEN = "abcdefghijkl"

val adjustAnalyticsModule = module {

    single<AdjustInstance> { Adjust.getDefaultInstance() }

    single<AdjustConfig> {
        AdjustConfig(androidContext(), APP_TOKEN, AdjustConfig.ENVIRONMENT_SANDBOX).apply {
            setLogLevel(LogLevel.VERBOSE)
        }
    }

    single<AdjustAnalyticsTrackerService> {
        adjustTracker(
            adjust = get(),
            featureEventFactories = getAll(),
            featurePropertyFactories = getAll(),
        )
    }

    single { AdjustAnalyticsService(get(), get()) }

    single<Herald.Provider>(named("adjust")) {
        val tracker = get<AdjustAnalyticsTrackerService>()
        val service = get<AdjustAnalyticsService>()
        Herald.Provider(
            name = "adjust",
            events = tracker,
            properties = tracker,
            identity = service,
            lifecycle = service,
            consent = service,
        )
    }
}

internal fun adjustTracker(
    adjust: AdjustInstance,
    featureEventFactories: List<AdjustEventTrackerFactory>,
    featurePropertyFactories: List<AdjustPropertySetterFactory>,
) = AdjustAnalyticsTrackerService(
    eventTrackerFactory = CompositeAdjustEventTrackerFactory(
        factories = buildList {
            add(AdRevenueAdjustEventTrackerFactory(adjust))
            addAll(featureEventFactories)
        },
    ),
    propertySetterFactory = CompositeAdjustPropertySetterFactory(
        factories = buildList {
            addAll(featurePropertyFactories)
            add(RequireMappedAdjustPropertySetterFactory)
        },
    ),
)

package com.moove.analytics.di

import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.common.Logger
import io.github.mkhytarmkhoian.herald.Herald
import io.github.mkhytarmkhoian.herald.amplitude.AmplitudeAnalyticsService
import io.github.mkhytarmkhoian.herald.amplitude.AmplitudeAnalyticsTrackerService
import io.github.mkhytarmkhoian.herald.amplitude.AmplitudeEventTrackerFactory
import io.github.mkhytarmkhoian.herald.amplitude.AmplitudePropertySetterFactory
import io.github.mkhytarmkhoian.herald.amplitude.CompositeAmplitudeEventTrackerFactory
import io.github.mkhytarmkhoian.herald.amplitude.CompositeAmplitudePropertySetterFactory
import io.github.mkhytarmkhoian.herald.amplitude.GenericAmplitudeEventTrackerFactory
import io.github.mkhytarmkhoian.herald.amplitude.GenericAmplitudePropertySetterFactory
import io.github.mkhytarmkhoian.herald.amplitude.ScreenViewAmplitudeEventTrackerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

// A placeholder: Amplitude queues and uploads, and its server rejects the key, which is enough to
// exercise the adapter end to end from Logcat.
private const val API_KEY = "moove-placeholder-api-key"

val amplitudeAnalyticsModule = module {

    // optOut = true: silent until consent. Amplitude doesn't persist it, so StartAnalyticsUseCase
    // re-applies the stored decision every launch. Screen-view autocapture stays off: Herald's
    // factory sends them.
    single<Amplitude> {
        Amplitude(Configuration(API_KEY, androidContext(), optOut = true)).apply {
            logger.logMode = Logger.LogMode.DEBUG
        }
    }

    single<AmplitudeAnalyticsTrackerService> {
        amplitudeTracker(
            amplitude = get(),
            featureEventFactories = getAll(),
            featurePropertyFactories = getAll(),
        )
    }

    single { AmplitudeAnalyticsService(get()) }

    single<Herald.Provider>(named("amplitude")) {
        val tracker = get<AmplitudeAnalyticsTrackerService>()
        val service = get<AmplitudeAnalyticsService>()
        Herald.Provider(
            name = "amplitude",
            events = tracker,
            properties = tracker,
            identity = service,
            lifecycle = service,
            consent = service,
        )
    }
}

/**
 * Amplitude is the warehouse: everything goes, under the generic terminator. Screen views come
 * first, because Amplitude has its own name for them.
 */
internal fun amplitudeTracker(
    amplitude: Amplitude,
    featureEventFactories: List<AmplitudeEventTrackerFactory>,
    featurePropertyFactories: List<AmplitudePropertySetterFactory>,
) = AmplitudeAnalyticsTrackerService(
    eventTrackerFactory = CompositeAmplitudeEventTrackerFactory(
        buildList {
            add(ScreenViewAmplitudeEventTrackerFactory(amplitude))
            addAll(featureEventFactories)
            add(GenericAmplitudeEventTrackerFactory(amplitude))
        },
    ),
    propertySetterFactory = CompositeAmplitudePropertySetterFactory(
        buildList {
            addAll(featurePropertyFactories)
            add(GenericAmplitudePropertySetterFactory(amplitude))
        },
    ),
)

package com.moove.analytics.di

import android.util.Log
import com.appsflyer.AppsFlyerLib
import io.github.mkhytarmkhoian.herald.Herald
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerAnalyticsService
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerAnalyticsTrackerService
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTrackerFactory
import io.github.mkhytarmkhoian.herald.appsflyer.CompositeAppsFlyerEventTrackerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

// A placeholder: AppsFlyer sends the launch and its server rejects the key, which is enough to
// exercise the adapter end to end from Logcat.
private const val DEV_KEY = "moove-placeholder-dev-key"

val appsFlyerAnalyticsModule = module {

    single<AppsFlyerLib>(createdAtStart = true) {
        AppsFlyerLib.getInstance().apply {
            setDebugLog(true)
            init(DEV_KEY, null, androidContext())
            registerSessionReadyListener { Log.d("analytics", "AppsFlyer session ready") }
        }
    }

    single<AppsFlyerAnalyticsTrackerService> {
        appsFlyerTracker(featureEventFactories = getAll())
    }

    single { AppsFlyerAnalyticsService(get(), androidContext()) }

    single<Herald.Provider>(named("appsflyer")) {
        val service = get<AppsFlyerAnalyticsService>()
        Herald.Provider(
            name = "appsflyer",
            events = get<AppsFlyerAnalyticsTrackerService>(),
            identity = service,
            lifecycle = service,
            consent = service,
        )
    }
}

/**
 * AppsFlyer is opt-in: only the conversions the features claim reach it.
 */
internal fun appsFlyerTracker(
    featureEventFactories: List<AppsFlyerEventTrackerFactory>,
) = AppsFlyerAnalyticsTrackerService(CompositeAppsFlyerEventTrackerFactory(featureEventFactories))

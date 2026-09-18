package com.moove.analytics.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import io.github.mkhytarmkhoian.herald.Herald
import io.github.mkhytarmkhoian.herald.firebase.CompositeFirebaseEventTrackerFactory
import io.github.mkhytarmkhoian.herald.firebase.CompositeFirebasePropertySetterFactory
import io.github.mkhytarmkhoian.herald.firebase.FirebaseAnalyticsService
import io.github.mkhytarmkhoian.herald.firebase.FirebaseAnalyticsTrackerService
import io.github.mkhytarmkhoian.herald.firebase.FirebaseEventTrackerFactory
import io.github.mkhytarmkhoian.herald.firebase.FirebasePropertySetterFactory
import io.github.mkhytarmkhoian.herald.firebase.GenericFirebaseEventTrackerFactory
import io.github.mkhytarmkhoian.herald.firebase.GenericFirebasePropertySetterFactory
import io.github.mkhytarmkhoian.herald.firebase.ScreenViewFirebaseEventTrackerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun hasFirebaseProject(context: Context): Boolean = FirebaseApp.getApps(context).isNotEmpty()

val firebaseAnalyticsModule = module {

    single<FirebaseAnalytics> { FirebaseAnalytics.getInstance(androidContext()) }

    single<FirebaseAnalyticsTrackerService> {
        firebaseTracker(
            analytics = get(),
            featureEventFactories = getAll(),
            featurePropertyFactories = getAll(),
        )
    }

    single { FirebaseAnalyticsService(get(), identificationEnabled = true) }

    single<Herald.Provider>(named("firebase")) {
        val tracker = get<FirebaseAnalyticsTrackerService>()
        val service = get<FirebaseAnalyticsService>()
        Herald.Provider(
            name = "firebase",
            events = tracker,
            properties = tracker,
            identity = service,
            lifecycle = service,
            consent = service,
        )
    }
}

internal fun firebaseTracker(
    analytics: FirebaseAnalytics,
    featureEventFactories: List<FirebaseEventTrackerFactory>,
    featurePropertyFactories: List<FirebasePropertySetterFactory>,
) = FirebaseAnalyticsTrackerService(
    eventTrackerFactory = CompositeFirebaseEventTrackerFactory(
        buildList {
            addAll(featureEventFactories)
            add(ScreenViewFirebaseEventTrackerFactory(analytics))
            add(GenericFirebaseEventTrackerFactory(analytics))
        },
    ),
    propertySetterFactory = CompositeFirebasePropertySetterFactory(
        buildList {
            addAll(featurePropertyFactories)
            add(GenericFirebasePropertySetterFactory(analytics))
        },
    ),
)

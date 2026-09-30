package com.moove.inspector.di

import com.moove.inspector.analytics.vendor.InspectorAdjustEventTrackerFactory
import com.moove.inspector.analytics.vendor.InspectorAppsFlyerEventTrackerFactory
import com.moove.inspector.presentation.InspectorNavigator
import com.moove.inspector.presentation.InspectorViewModel
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerEventTrackerFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val ANALYTICS_CONTRIBUTOR = "inspector"

val inspectorModule = module {

    factory { InspectorNavigator(screenNavigator = get()) }
    viewModel {
        InspectorViewModel(
            exceptionHandler = get(),
            recentEvents = get(),
            analyticsEventService = get(),
            analyticsLifecycleService = get(),
        )
    }

    // region analytics
    factory<AdjustEventTrackerFactory>(named(ANALYTICS_CONTRIBUTOR)) { InspectorAdjustEventTrackerFactory(get()) }
    factory<AppsFlyerEventTrackerFactory>(named(ANALYTICS_CONTRIBUTOR)) { InspectorAppsFlyerEventTrackerFactory(get()) }
    // endregion
}

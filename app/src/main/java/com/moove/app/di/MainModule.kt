package com.moove.app.di

import com.moove.app.analytics.vendor.AppAdjustEventTrackerFactory
import com.moove.app.analytics.vendor.AppAdjustPropertySetterFactory
import com.moove.app.feature.home.HomeNavigator
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetterFactory
import com.moove.app.feature.home.HomeViewModel
import com.moove.app.feature.inspector.InspectorNavigator
import com.moove.app.feature.inspector.InspectorViewModel
import com.moove.app.main.MainActivityViewModel
import com.moove.app.main.MainNavigator
import com.moove.app.navigation.AppNavigator
import com.moove.shared.navigation.GlobalAppNavigator
import com.moove.shared.navigation.MoviesNavigator
import com.moove.shared.navigation.ScreenNavigator
import com.moove.shared.navigation.TicketsNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.binds
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {

    factory {
        AppNavigator(navController = get())
    } binds arrayOf(
        ScreenNavigator::class,
        GlobalAppNavigator::class,
        TicketsNavigator::class,
        MoviesNavigator::class,
    )

    factory {
        HomeNavigator(
            navController = get(),
            screenNavigator = get(),
        )
    }
    viewModel {
        HomeViewModel(
            exceptionHandler = get(),
            analyticsEventService = get(),
            analyticsPropertyService = get(),
            analyticsIdentityService = get(),
            getAnalyticsConsentUseCase = get(),
            setAnalyticsConsentUseCase = get(),
        )
    }
    factory { InspectorNavigator(screenNavigator = get()) }
    viewModel {
        InspectorViewModel(
            exceptionHandler = get(),
            recentEvents = get(),
            analyticsEventService = get(),
            analyticsLifecycleService = get(),
        )
    }

    viewModel {
        MainActivityViewModel(
            exceptionHandler = get(),
            getDeeplinkUseCase = get(),
            startAnalyticsUseCase = get(),
            analyticsEventService = get(),
        )
    }
    factoryOf(::MainNavigator)

    // The app module's own vendor mappings, collected by :analytics like a feature's. Qualified,
    // because an unqualified definition of the same type in another module would override it.
    factory<AdjustEventTrackerFactory>(named("app")) { AppAdjustEventTrackerFactory(adjust = get()) }
    factory<AdjustPropertySetterFactory>(named("app")) { AppAdjustPropertySetterFactory() }
}

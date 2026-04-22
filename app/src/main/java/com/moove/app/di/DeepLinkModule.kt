package com.moove.app.di

import com.moove.BuildConfig
import com.moove.app.feature.deeplink.data.DeeplinkDataRepository
import com.moove.app.feature.deeplink.data.DynamicLinkDataRepository
import com.moove.app.feature.deeplink.data.local.AppDeepLinkLocalDataSource
import com.moove.app.feature.deeplink.data.remote.FirebaseDynamicLinkDataSource
import com.moove.app.feature.deeplink.presentation.DeepLinkAppNavigator
import com.moove.shared.feature.deeplink.domain.DeeplinkRepository
import com.moove.shared.feature.deeplink.domain.DynamicLinkRepository
import com.moove.shared.feature.deeplink.domain.GetDeeplinkUseCase
import com.moove.shared.feature.deeplink.domain.GetDynamicLinkUseCase
import com.moove.shared.feature.deeplink.presentation.DeepLinkNavigator
import org.koin.dsl.module

val deepLinkModule = module {

    factory<DeeplinkRepository> { DeeplinkDataRepository(get()) }
    factory<DynamicLinkRepository> { DynamicLinkDataRepository(get()) }
    factory<DeepLinkNavigator> {
        DeepLinkAppNavigator(
            ticketsNavigator = get(),
            globalAppNavigator = get(),
            moviesNavigator = get(),
        )
    }
    factory {
        FirebaseDynamicLinkDataSource(
            host = BuildConfig.FIREBASE_DYNAMIC_LINK_HOST,
        )
    }
    factory { AppDeepLinkLocalDataSource() }
    factory {
        GetDeeplinkUseCase(
            deeplinkRepository = get(),
            getDynamicLinkUseCase = get(),
        )
    }
    factory { GetDynamicLinkUseCase(get()) }
}

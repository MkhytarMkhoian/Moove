package com.moove.analytics.di

import android.content.Context
import com.moove.analytics.BuildConfig
import com.moove.analytics.data.AnalyticsConsentDataRepository
import com.moove.analytics.data.local.AnalyticsConsentLocalDataSource
import com.moove.analytics.domain.AnalyticsConsentRepository
import com.moove.analytics.domain.use_cases.GetAnalyticsConsentUseCase
import com.moove.analytics.domain.use_cases.RestoreAnalyticsConsentUseCase
import com.moove.analytics.domain.use_cases.SetAnalyticsConsentUseCase
import com.moove.analytics.domain.use_cases.StartAnalyticsUseCase
import io.github.mkhytarmkhoian.herald.AnalyticsLifecycleService
import io.github.mkhytarmkhoian.herald.ConsentService
import io.github.mkhytarmkhoian.herald.EventTrackerService
import io.github.mkhytarmkhoian.herald.Herald
import io.github.mkhytarmkhoian.herald.IdentifiableUserService
import io.github.mkhytarmkhoian.herald.PropertyTrackerService
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module

/**
 * The analytics composition root.
 *
 * Each vendor is its own module and contributes one [Herald.Provider] under its own qualifier;
 * `getAll` collects them here into the one Herald, bound to every capability interface. Nothing
 * else is bound to those interfaces — an adapter bound directly would let a class quietly reach
 * one vendor alone. This module names no feature event.
 *
 * The log provider is a debug-build vendor: it prints every call to Logcat and has no business in
 * a release. The [Context] is only for deciding whether Firebase can be included: this showcase
 * ships no `google-services.json`, and `FirebaseAnalytics.getInstance` throws without one. A real
 * app has Firebase or it does not; it never checks at runtime.
 */
fun analyticsModule(context: Context) = module {

    includes(inspectorAnalyticsModule, mixpanelAnalyticsModule, adjustAnalyticsModule)
    if (BuildConfig.DEBUG) includes(logAnalyticsModule)
    if (hasFirebaseProject(context)) includes(firebaseAnalyticsModule)

    // region consent
    single { AnalyticsConsentLocalDataSource(androidContext()) }
    single<AnalyticsConsentRepository> { AnalyticsConsentDataRepository(localDataSource = get()) }
    factory { GetAnalyticsConsentUseCase(analyticsConsentRepository = get()) }
    factory {
        SetAnalyticsConsentUseCase(
            analyticsConsentRepository = get(),
            analyticsConsentService = get()
        )
    }
    factory {
        RestoreAnalyticsConsentUseCase(
            analyticsConsentRepository = get(),
            analyticsConsentService = get()
        )
    }
    // endregion

    single {
        Herald {
            providers(getAll<Herald.Provider>())
            dispatcher(Dispatchers.IO)
            errorReporter(get())
        }
    } binds arrayOf(
        EventTrackerService::class,
        PropertyTrackerService::class,
        IdentifiableUserService::class,
        AnalyticsLifecycleService::class,
        ConsentService::class,
    )

    factory {
        StartAnalyticsUseCase(
            analyticsLifecycleService = get(),
            restoreAnalyticsConsentUseCase = get()
        )
    }
}

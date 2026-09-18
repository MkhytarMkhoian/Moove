package com.moove.analytics.di

import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.mixpanel.android.mpmetrics.MixpanelOptions
import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.Herald
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.ScreenViewEvent
import io.github.mkhytarmkhoian.herald.mixpanel.CompositeMixpanelEventTrackerFactory
import io.github.mkhytarmkhoian.herald.mixpanel.CompositeMixpanelPropertySetterFactory
import io.github.mkhytarmkhoian.herald.mixpanel.GenericMixpanelEventTrackerFactory
import io.github.mkhytarmkhoian.herald.mixpanel.GenericMixpanelPropertySetterFactory
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelAnalyticsService
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelAnalyticsTrackerService
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelEventTracker
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelEventTrackerFactory
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelPropertySetterFactory
import io.github.mkhytarmkhoian.herald.mixpanel.UserPropertyMixpanelPropertySetterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val PROJECT_TOKEN = "moove-placeholder-token"

val mixpanelAnalyticsModule = module {

    // optOutTrackingDefault(true) is the consent decision Herald documents and cannot make for us:
    // a fresh install collects nothing until ConsentService.setEnabled(true), and Mixpanel
    // persists the opt-in from then on. Automatic events are off so the log shows only what
    // Herald sends.
    single<MixpanelAPI> {
        MixpanelAPI.getInstance(
            androidContext(),
            PROJECT_TOKEN,
            false,
            MixpanelOptions.Builder().optOutTrackingDefault(true).build(),
        )
    }

    single<MixpanelAnalyticsTrackerService> {
        mixpanelTracker(
            mixpanel = get(),
            featureEventFactories = getAll(),
            featurePropertyFactories = getAll(),
        )
    }

    single { MixpanelAnalyticsService(get(), loggingEnabled = true, identificationEnabled = true) }

    single<Herald.Provider>(named("mixpanel")) {
        val tracker = get<MixpanelAnalyticsTrackerService>()
        val service = get<MixpanelAnalyticsService>()
        Herald.Provider(
            name = "mixpanel",
            events = tracker,
            properties = tracker,
            identity = service,
            lifecycle = service,
            consent = service,
        )
    }
}

/**
 * Mixpanel takes everything except screen views, dropped ahead of the feature factories so no
 * feature can send one by accident. The property chain is where order matters: the `UserProperty`
 * factory must precede the generic one, or a `UserProperty` is registered as a super property and
 * the people profile is never written to.
 */
internal fun mixpanelTracker(
    mixpanel: MixpanelAPI,
    featureEventFactories: List<MixpanelEventTrackerFactory>,
    featurePropertyFactories: List<MixpanelPropertySetterFactory>,
) = MixpanelAnalyticsTrackerService(
    eventTrackerFactory = CompositeMixpanelEventTrackerFactory(
        buildList {
            add(DropScreenViewsMixpanelEventTrackerFactory)
            addAll(featureEventFactories)
            add(GenericMixpanelEventTrackerFactory(mixpanel))
        },
    ),
    propertySetterFactory = CompositeMixpanelPropertySetterFactory(
        buildList {
            addAll(featurePropertyFactories)
            add(UserPropertyMixpanelPropertySetterFactory(mixpanel))
            add(GenericMixpanelPropertySetterFactory(mixpanel))
        },
    ),
)

/** Mixpanel bills per event and screen views are noise there, so they never leave the app for it. */
internal object DropScreenViewsMixpanelEventTrackerFactory : MixpanelEventTrackerFactory {

    override fun create(event: Event): Resolution<MixpanelEventTracker> =
        if (event is ScreenViewEvent) Resolution.Dropped else Resolution.Declined
}

package com.moove.tickets.di

import com.moove.tickets.data.TicketsDataRepository
import com.moove.tickets.data.local.TicketsLocalDataSource
import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.use_cases.BuyTicketUseCase
import com.moove.tickets.domain.use_cases.GetFaresByIdUseCase
import com.moove.tickets.domain.use_cases.GetRydersUseCase
import com.moove.tickets.presentation.confirmation.ConfirmationNavigator
import com.moove.tickets.presentation.confirmation.ConfirmationViewModel
import com.moove.tickets.presentation.fare.FareListNavigator
import com.moove.tickets.presentation.fare.FareListViewModel
import com.moove.tickets.presentation.list.RyderListNavigator
import com.moove.tickets.presentation.list.RyderListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import com.moove.tickets.analytics.vendor.TicketsAdjustEventTrackerFactory
import com.moove.tickets.analytics.vendor.TicketsAdjustPropertySetterFactory
import com.moove.tickets.analytics.vendor.TicketsFirebaseEventTrackerFactory
import com.moove.tickets.analytics.vendor.TicketsMixpanelEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.AdjustEventTrackerFactory
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetterFactory
import io.github.mkhytarmkhoian.herald.firebase.FirebaseEventTrackerFactory
import io.github.mkhytarmkhoian.herald.mixpanel.MixpanelEventTrackerFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val ANALYTICS_CONTRIBUTOR = "tickets"

val ticketsModule = module {

    // region domain
    factory { GetRydersUseCase(get()) }
    factory { GetFaresByIdUseCase(get()) }
    factory { BuyTicketUseCase(get()) }
    // endregion

    // region data
    single { TicketsLocalDataSource(get()) }
    single<TicketsRepository> { TicketsDataRepository(get()) }
    // endregion

    factory { RyderListNavigator(get(), get()) }
    viewModel {
        RyderListViewModel(
            exceptionHandler = get(),
            getRydersUseCase = get(),
            analyticsEventService = get(),
        )
    }

    factory { FareListNavigator(get(), get()) }
    viewModel {
        FareListViewModel(
            exceptionHandler = get(),
            ryderId = get(),
            getFaresByIdUseCase = get(),
            analyticsEventService = get(),
        )
    }

    factory { ConfirmationNavigator(get()) }
    viewModel {
        ConfirmationViewModel(
            exceptionHandler = get(),
            ryderId = get(),
            fare = get(),
            buyTicketUseCase = get(),
            analyticsEventService = get(),
            analyticsPropertyService = get(),
        )
    }

    // region analytics
    // The feature's per-vendor mappings, bound as the vendor factory types so the analytics
    // composition root can collect them with getAll() without knowing this module exists.
    // Qualified with the feature's name: two unqualified definitions of one type override each
    // other in Koin, and the last module loaded would silently win.
    factory<FirebaseEventTrackerFactory>(named(ANALYTICS_CONTRIBUTOR)) { TicketsFirebaseEventTrackerFactory(get()) }
    factory<AdjustEventTrackerFactory>(named(ANALYTICS_CONTRIBUTOR)) { TicketsAdjustEventTrackerFactory(get()) }
    factory<AdjustPropertySetterFactory>(named(ANALYTICS_CONTRIBUTOR)) { TicketsAdjustPropertySetterFactory(get()) }
    factory<MixpanelEventTrackerFactory>(named(ANALYTICS_CONTRIBUTOR)) { TicketsMixpanelEventTrackerFactory(get()) }
    // endregion
}

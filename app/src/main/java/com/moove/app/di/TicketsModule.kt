package com.moove.app.di

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
import org.koin.dsl.module

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

    factory { RyderListNavigator(get()) }
    viewModel {
        RyderListViewModel(
            exceptionHandler = get(),
            getRydersUseCase = get(),
        )
    }

    factory { FareListNavigator(get()) }
    viewModel {
        FareListViewModel(
            exceptionHandler = get(),
            ryderId = get(),
            getFaresByIdUseCase = get(),
        )
    }

    factory { ConfirmationNavigator(get()) }
    viewModel {
        ConfirmationViewModel(
            exceptionHandler = get(),
            ryderId = get(),
            fare = get(),
            buyTicketUseCase = get(),
        )
    }
}

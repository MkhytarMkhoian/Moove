package com.moove.app.di

import com.moove.tickets.data.TicketsDataRepository
import com.moove.tickets.data.local.TicketsLocalDataSource
import com.moove.tickets.domain.TicketsRepository
import com.moove.tickets.domain.use_cases.GetFaresByIdUseCase
import com.moove.tickets.domain.use_cases.GetRydersUseCase
import com.moove.tickets.presentation.list.RyderListNavigator
import com.moove.tickets.presentation.list.RyderListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ticketsModule = module {

    // region domain
    factory { GetRydersUseCase(get()) }
    factory { GetFaresByIdUseCase(get()) }
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
}

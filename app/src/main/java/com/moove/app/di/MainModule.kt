package com.moove.app.di

import com.moove.app.feature.home.HomeNavigator
import com.moove.app.feature.home.HomeViewModel
import com.moove.app.main.MainActivityViewModel
import com.moove.app.main.MainNavigator
import com.moove.app.navigation.AppNavigator
import com.moove.shared.navigation.GlobalAppNavigator
import com.moove.shared.navigation.ScreenNavigator
import com.moove.shared.navigation.TicketsNavigator
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.binds
import org.koin.dsl.module

val mainModule = module {

    factory {
        AppNavigator(
            navController = get(),
        )
    } binds arrayOf(
        ScreenNavigator::class,
        GlobalAppNavigator::class,
        TicketsNavigator::class,
    )

    factory {
        HomeNavigator(
            navController = get(),
            screenNavigator = get(),
        )
    }
    viewModelOf(::HomeViewModel)

    viewModelOf(::MainActivityViewModel)
    factoryOf(::MainNavigator)
}

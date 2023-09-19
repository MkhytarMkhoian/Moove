package com.moove.app.di

import android.app.Activity
import com.moove.app.main.AppNavigator
import com.moove.app.main.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {

    viewModelOf(::MainActivityViewModel)

    factory {
        AppNavigator(
            navController = get(),
            coroutineScope = get(),
            context = get<Activity>(),
        )
    }
}

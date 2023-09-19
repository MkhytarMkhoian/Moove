package com.moove.app.di

import com.moove.app.shared.exception_handler.main.MainExceptionHandler
import com.moove.core.exception.ExceptionHandler
import org.koin.dsl.module

val exceptionsModule = module {
    single { listOf<ExceptionHandler>() }
    single<ExceptionHandler> { MainExceptionHandler(get()) }
}

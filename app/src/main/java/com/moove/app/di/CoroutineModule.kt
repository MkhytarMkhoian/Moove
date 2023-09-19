package com.moove.app.di

import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.core.kotlin.coroutines.AppCoroutineScope
import org.koin.dsl.module

val coroutineModule = module {
    single { AppCoroutineScope(exceptionHandler = get()) }
    single { get<ExceptionHandler>().asCoroutineExceptionHandler() }
}

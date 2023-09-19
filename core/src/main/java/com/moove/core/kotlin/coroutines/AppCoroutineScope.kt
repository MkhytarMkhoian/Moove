package com.moove.core.kotlin.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * App-wide coroutine scope intended to be a singleton.
 * It is supervisor scope, meaning any unhandled exception wont affect siblings.
 * Also, it requires exception handler to exist meaning we want all un-handled exceptions to be caught.
 */
class AppCoroutineScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    exceptionHandler: CoroutineExceptionHandler,
) : CoroutineScope by CoroutineScope(
    SupervisorJob() + dispatcher + exceptionHandler + CoroutineName("AppScope")
)

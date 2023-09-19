package com.moove.core.exception

import kotlinx.coroutines.CoroutineExceptionHandler

fun interface ExceptionHandler {
    fun handle(error: Throwable)
}

fun ExceptionHandler.asCoroutineExceptionHandler() =
    CoroutineExceptionHandler { _, throwable -> this.handle(throwable) }

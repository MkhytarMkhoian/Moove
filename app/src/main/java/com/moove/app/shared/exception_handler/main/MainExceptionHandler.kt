package com.moove.app.shared.exception_handler.main

import com.moove.core.exception.ExceptionHandler
import com.moove.core.okhttp.NoConnectionException

class MainExceptionHandler(
    private val exceptionHandlers: List<ExceptionHandler>
) : ExceptionHandler {

    override fun handle(error: Throwable) {
        if (error is NoConnectionException) {
            return
        }
        exceptionHandlers.forEach { it.handle(error) }
    }
}

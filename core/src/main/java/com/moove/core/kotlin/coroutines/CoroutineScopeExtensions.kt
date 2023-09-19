package com.moove.core.kotlin.coroutines

import com.moove.core.kotlin.runSuspendCatching
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

@Suppress("RedundantSuspendModifier", "SuspendFunctionOnCoroutineScope")
suspend inline fun <R> CoroutineScope.executeUseCase(block: () -> R): Result<R> {
    val currentExecContext = coroutineScope { coroutineContext }
    val exceptionHandler = currentExecContext[CoroutineExceptionHandler]
    return runSuspendCatching(block)
        .onFailure {
            exceptionHandler?.handleException(currentExecContext, it)
        }
}

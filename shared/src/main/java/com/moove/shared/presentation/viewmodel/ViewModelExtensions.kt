package com.moove.shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.kotlin.runSuspendCatching

/**
 * Runs a use case, reporting a failure to [exceptionHandler] and returning it as a [Result].
 *
 * The handler is explicit rather than read from the coroutine context: since Orbit 8 an intent's
 * context no longer carries the container's exception handler, so a context lookup would find the
 * ViewModel scope's (none) or, in a test, the test framework's.
 */
suspend inline fun <R> ViewModel.executeUseCase(
    exceptionHandler: ExceptionHandler,
    block: () -> R,
): Result<R> = runSuspendCatching(block).onFailure(exceptionHandler::handle)

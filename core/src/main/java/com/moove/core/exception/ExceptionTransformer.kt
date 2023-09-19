package com.moove.core.exception

fun interface ExceptionTransformer {
    fun transform(input: Throwable): Throwable
}

fun ExceptionTransformer.decorate(block: (ExceptionTransformer) -> ExceptionTransformer) = block(this)

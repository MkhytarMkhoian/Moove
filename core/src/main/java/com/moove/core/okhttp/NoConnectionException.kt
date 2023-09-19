package com.moove.core.okhttp

import java.io.IOException

class NoConnectionException(
    message: String? = null,
    cause: Throwable? = null
) : IOException(message, cause)

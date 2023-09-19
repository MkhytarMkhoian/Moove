package com.moove.core.okhttp

import java.io.IOException

class ConnectionErrorInterceptor : okhttp3.Interceptor {

    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        try {
            return chain.proceed(chain.request())
        } catch (exception: IOException) {
            throw NoConnectionException(cause = exception)
        }
    }
}

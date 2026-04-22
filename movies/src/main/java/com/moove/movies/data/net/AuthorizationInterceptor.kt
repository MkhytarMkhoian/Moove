package com.moove.movies.data.net

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val apiKey: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestWithAuth = original.newBuilder()
            .header(HEADER_AUTHORIZATION, "Bearer $apiKey")
            .build()
        return chain.proceed(requestWithAuth)
    }

    private companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
    }
}

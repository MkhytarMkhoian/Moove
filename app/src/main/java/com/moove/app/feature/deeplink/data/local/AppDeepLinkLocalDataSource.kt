package com.moove.app.feature.deeplink.data.local

import com.moove.app.feature.deeplink.domain.AppDeepLink
import com.moove.core.kotlin.text.matchesPattern
import com.moove.shared.feature.deeplink.domain.DeepLink
import com.moove.tickets.domain.model.Fare
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class AppDeepLinkLocalDataSource(
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    companion object {
        private const val RYDER_ID = "ryderId"
        private const val PRICE = "price"

        const val HOME = "moove://app/home"
        const val FARE_LIST = "moove://app/fare_list"
        const val CONFIRM_CONFIRMATION = "/ticket/confirmation"
        const val MOOVE_CONFIRM_CONFIRMATION = "moove://app/confirmation"
    }

    suspend fun getDeepLinkData(uri: String): DeepLink = withContext(backgroundDispatcher) {
        when {
            uri.matchesPattern(CONFIRM_CONFIRMATION) -> {
                val innerUri = URI.create(uri)
                val params = getQueryParams(innerUri)
                AppDeepLink.Confirmation(
                    ryderId = params[RYDER_ID]!!,
                    fare = Fare(
                        description = "",
                        price = params[PRICE]?.toFloat()!!
                    ),
                )
            }

            uri.matchesPattern(MOOVE_CONFIRM_CONFIRMATION) -> {
                val innerUri = URI.create(uri)
                val params = getQueryParams(innerUri)
                AppDeepLink.Confirmation(
                    ryderId = params[RYDER_ID]!!,
                    fare = Fare(
                        description = "",
                        price = params[PRICE]?.toFloat()!!
                    ),
                )
            }

            uri.isThat(FARE_LIST) -> {
                val innerUri = URI.create(uri)
                val params = getQueryParams(innerUri)
                AppDeepLink.FareList(ryderId = params[RYDER_ID]!!)
            }

            uri.isThat(HOME) || uri.matchesPattern(HOME) -> AppDeepLink.Home
            else -> AppDeepLink.Unknown
        }
    }

    private fun String.isThat(type: String): Boolean {
        /**
         * Handle two cases with slash symbol at the end and without it
         * app/home/ and app/home
         */
        return contains(type, ignoreCase = true)
    }

    private fun getQueryParams(url: URI): Map<String, String> {
        val query = url.query ?: return emptyMap()
        return query
            .split("&".toRegex())
            .filter { it.isNotEmpty() }
            .map(::mapQueryParameter)
            .associateBy(keySelector = { it.first }, valueTransform = { it.second })
    }

    private fun mapQueryParameter(query: String): Pair<String, String> {
        val index = query.indexOf("=")
        val key = if (index > 0) query.substring(0, index) else query
        val value = if (index > 0 && query.length > index + 1) {
            query.substring(index + 1)
        } else null
        return Pair(
            URLDecoder.decode(key, StandardCharsets.UTF_8.name()),
            URLDecoder.decode(value, StandardCharsets.UTF_8.name())
        )
    }
}

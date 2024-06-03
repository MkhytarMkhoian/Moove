package com.moove.shared.feature.deeplink.domain

interface DeeplinkRepository {

    suspend fun getDeepLink(uri: String): DeepLink
}

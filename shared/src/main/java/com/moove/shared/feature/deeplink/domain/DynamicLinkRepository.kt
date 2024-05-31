package com.moove.shared.feature.deeplink.domain

interface DynamicLinkRepository {

    suspend fun parseLink(uri: String): String?
}

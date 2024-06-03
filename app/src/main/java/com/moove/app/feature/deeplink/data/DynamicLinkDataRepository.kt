package com.moove.app.feature.deeplink.data

import com.moove.app.feature.deeplink.data.remote.FirebaseDynamicLinkDataSource
import com.moove.shared.feature.deeplink.domain.DynamicLinkRepository

class DynamicLinkDataRepository(
    private val dataSource: FirebaseDynamicLinkDataSource,
) : DynamicLinkRepository {

    override suspend fun parseLink(uri: String): String? {
        return dataSource.parseLink(uri)
    }
}

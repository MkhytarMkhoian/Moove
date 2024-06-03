package com.moove.app.feature.deeplink.data

import com.moove.app.feature.deeplink.data.local.AppDeepLinkLocalDataSource
import com.moove.shared.feature.deeplink.domain.DeepLink
import com.moove.shared.feature.deeplink.domain.DeeplinkRepository

class DeeplinkDataRepository(
    private val localDataSource: AppDeepLinkLocalDataSource,
) : DeeplinkRepository {

    override suspend fun getDeepLink(uri: String): DeepLink {
        return localDataSource.getDeepLinkData(uri)
    }
}

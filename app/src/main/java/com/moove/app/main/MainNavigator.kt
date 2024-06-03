package com.moove.app.main

import com.moove.shared.feature.deeplink.domain.DeepLink
import com.moove.shared.feature.deeplink.presentation.DeepLinkNavigator

class MainNavigator(
    private val deepLinkNavigator: DeepLinkNavigator,
) {

    fun navigateDeepLink(link: DeepLink) {
        deepLinkNavigator.navigateTo(link)
    }
}
